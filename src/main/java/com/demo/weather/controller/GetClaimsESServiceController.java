package com.demo.weather.controller;

import com.optum.eaip.fin.billing.claimpayments.model.ElasticSearchInput;
import com.optum.eaip.fin.billing.claimpayments.model.ElasticSearchResults;
import com.optum.eaip.fin.billing.claimpayments.utils.GatewayConstants;
import com.optum.eaip.fin.billing.claimpayments.utils.GatewayUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static com.optum.eaip.fin.billing.claimpayments.utils.GatewayConstants.*;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Component
public class GetClaimsESServiceController {

  private static final Logger logger = LogManager.getLogger("com.optum.eaip.BackendCollector");

  @Autowired RestHighLevelClient restHighLevelClient;

  @Value("${es.ELASTIC_SEARCH_DB}")
  private String elasticSearchDb;

  public ElasticSearchResults getClaims(ElasticSearchInput esSearchInput) {

    GatewayUtil gatewayUtil = new GatewayUtil();
    ArrayList<String> results = new ArrayList<>();
    ElasticSearchResults esResults = new ElasticSearchResults();

    try {

      long startTime = System.currentTimeMillis();

      RestHighLevelClient newClient = restHighLevelClient;

      BoolQueryBuilder fqb = validateQuery(esSearchInput, gatewayUtil);

      int maxRecords = Integer.parseInt(esSearchInput.getMaxRecords());
      SearchRequest searchRequest = new SearchRequest(elasticSearchDb);

      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      searchSourceBuilder.query(fqb);
      searchSourceBuilder.size(maxRecords);
      searchRequest.source(searchSourceBuilder);

      callElasticSearch(
          esSearchInput, searchRequest, results, esResults, newClient, startTime, fqb);

    } catch (ElasticsearchException ex) {
      logger.error(ex.toString());
      esResults.setStatusCode(GatewayConstants.StatusCode.BACKEND);
    }

    esResults.setEntityName(esSearchInput.getEntyName());
    esResults.setFname(esSearchInput.getProviderFirstName());
    esResults.setLname(esSearchInput.getProviderLastName());
    esResults.setMname(esSearchInput.getProviderMidleName());
    esResults.setTinNumber(esSearchInput.getTinNumber());
    esResults.setSeriesDesign(esSearchInput.getSeriesDesignator());
    esResults.setCheckNo(esSearchInput.getCheckNumber());

    esResults.setResults(results);
    return esResults;
  }

  private void callElasticSearch(
      ElasticSearchInput esSearchInput,
      SearchRequest searchRequest,
      ArrayList<String> results,
      ElasticSearchResults esResults,
      RestHighLevelClient newClient,
      long startTime,
      BoolQueryBuilder fqb) {

    int maxRecords = Integer.parseInt(esSearchInput.getMaxRecords());

    try {
      logger.info("Calling ElasticSearch to get claims data ");

      SearchResponse sr = newClient.search(searchRequest, RequestOptions.DEFAULT);

      long executionTime = System.currentTimeMillis() - startTime;

      logExecutionTime(executionTime, fqb);

      checkHitRecord(sr, esResults, maxRecords);

      if (sr.getHits().getHits().length > 0) {
        for (SearchHit hits : sr.getHits().getHits()) {

          String claimDoc = hits.getSourceAsString();

          if (checkParameter(esSearchInput)) {

            Map<String, SearchHits> coll = hits.getInnerHits();

            /* When using inner hits the elastic search would give a map of json object with
             * the type being the name of the inner table and the value being the json
             * since their are many json objects.
             * to parse them we need to make them in to a single json
             * The below code does that.
             */
            if (coll.get(PAYMENT_INFO) != null) {
              checkPaymentHit(coll, claimDoc, results);

            } else if (coll.get(PROVIDER_INFO) != null
                && coll.get(PROVIDER_INFO).getHits() != null) {

              appendPrvDocument(coll, results, claimDoc);
            }
          } else {
            results.add(hits.getSourceAsString());
          }
        }

        esResults.setStatusCode(GatewayConstants.StatusCode.SUCCESS);

      } else {
        esResults.setStatusCode(GatewayConstants.StatusCode.NORECORDS);
      }

    } catch (IOException | ElasticsearchException ex) {
      logger.error(ex.getMessage());
      esResults.setStatusCode(GatewayConstants.StatusCode.BACKEND);
    }
  }

  private void logExecutionTime(long executionTime, BoolQueryBuilder fqb) {
    logger.info("GetClaims ElasticSearch took {} ", executionTime);

    if (executionTime >= 6000) {
      logger.info("GetClaims ElasticSearch took more than 6 Seconds for Query {}", fqb);
    }
  }

  private boolean checkParameter(ElasticSearchInput esSearchInput) {
    return (isNotBlank(esSearchInput.getTinNumber())
        || isNotBlank(esSearchInput.getEntyName())
        || isNotBlank(esSearchInput.getProviderFirstName())
        || isNotBlank(esSearchInput.getProviderLastName())
        || isNotBlank(esSearchInput.getProviderMidleName())
        || isNotBlank(esSearchInput.getCheckNumber())
        || isNotBlank(esSearchInput.getSeriesDesignator()));
  }

  private void appendPrvDocument(
      Map<String, SearchHits> coll, ArrayList<String> results, String claimDoc) {
    String sbToString = null;

    StringBuilder sb = new StringBuilder(claimDoc);
    sb.setCharAt(claimDoc.length() - 1, ',');

    String prv = "prv";
    sb.append("\"" + prv + "\"");
    sb.append(":[");

    SearchHit[] sehit = coll.get(PROVIDER_INFO).getHits();

    appendDocument(sehit, sb);

    sbToString = sb.toString();
    results.add(sbToString);
  }

  private void checkPaymentHit(
      Map<String, SearchHits> coll, String claimDoc, ArrayList<String> results) {
    String sbToString = null;
    if (coll.get(PAYMENT_INFO).getHits() != null) {

      StringBuilder sb = new StringBuilder(claimDoc);

      sb.setCharAt(claimDoc.length() - 1, ',');

      String pay = "pay";
      sb.append("\"" + pay + "\"");
      sb.append(":[");

      SearchHit[] sehit = coll.get(PAYMENT_INFO).getHits();

      appendDocument(sehit, sb);

      sbToString = sb.toString();

      addProviderInfo(coll, results, sbToString);
    }
  }

  private void addProviderInfo(
      Map<String, SearchHits> coll, ArrayList<String> results, String sbToString) {
    if (coll.get(PROVIDER_INFO) != null) {
      checkProviderHit(coll, sbToString, results);

    } else {
      results.add(sbToString);
    }
  }

  private void checkProviderHit(
      Map<String, SearchHits> coll, String sbToString, ArrayList<String> results) {
    SearchHit[] sehit = null;
    if (coll.get(PROVIDER_INFO).getHits() != null) {

      StringBuilder sb2 = new StringBuilder(sbToString);
      sb2.setCharAt(sbToString.length() - 1, ',');

      String prv = "prv";
      sb2.append("\"" + prv + "\"");
      sb2.append(":[");

      sehit = coll.get(PROVIDER_INFO).getHits();

      appendDocument(sehit, sb2);

      sbToString = sb2.toString();
      results.add(sbToString);
    }
  }

  private void appendDocument(SearchHit[] sehit, StringBuilder sb) {
    for (int i = 0; i < sehit.length; i++) {

      if (i > 0 && i < sehit.length) {
        sb.append(",");
      }
      sb.append(sehit[i].getSourceAsString());
    }
    sb.append("]}");
  }

  private void checkHitRecord(SearchResponse sr, ElasticSearchResults esResults, int maxRecords) {
    if (sr.getHits().getTotalHits().value > GatewayConstants.NUMBER
        || sr.getHits().getTotalHits().value > maxRecords) {
      esResults.setMoreRecords(GatewayConstants.MORERECORDS);
    }
  }

  private BoolQueryBuilder validateQuery(
      ElasticSearchInput esSearchInput, GatewayUtil gatewayUtil) {
    RangeQueryBuilder dateRangeBuilder = null;
    RangeQueryBuilder memDobfind = null;

    BoolQueryBuilder bqb = QueryBuilders.boolQuery();
    BoolQueryBuilder nested2 = QueryBuilders.boolQuery();
    BoolQueryBuilder nested3 = QueryBuilders.boolQuery();

    InnerHitBuilder innerHitBuilder = new InnerHitBuilder();

    mapStringQuery(bqb, esSearchInput);
    mapPrefixQuery(bqb, esSearchInput);

    mapNested3PrefixQuery(nested3, esSearchInput);

    mapNested2PrefixQuery(nested2, esSearchInput);

    memDobfind = mapMemDobRangeQuery(memDobfind, esSearchInput);

    mapMultiMatchQuery(bqb, esSearchInput);

    dateRangeBuilder = mapDateRange(esSearchInput, gatewayUtil);

    NestedQueryBuilder nfb = null;

    nfb = mapNestedQuery(esSearchInput, nested2, innerHitBuilder);

    NestedQueryBuilder nfb2 = null;

    nfb2 = mapNestedBoostQuery(esSearchInput, nested3, innerHitBuilder);

    // Filter that combines all the filters
    BoolQueryBuilder filter = null;

    if (nfb != null) {

      filter = checkQuery(nfb2, memDobfind, nfb, dateRangeBuilder);

    } else {
      if (nfb2 != null) {
        if (memDobfind != null) {
          filter = QueryBuilders.boolQuery().must(dateRangeBuilder).must(nfb2).must(memDobfind);
        } else {
          filter = QueryBuilders.boolQuery().must(dateRangeBuilder).must(nfb2);
        }
      } else {
        if (memDobfind != null) {
          filter = QueryBuilders.boolQuery().must(dateRangeBuilder).must(memDobfind);
        } else {
          filter = QueryBuilders.boolQuery().must(dateRangeBuilder);
        }
      }
    }

    return (QueryBuilders.boolQuery().filter(bqb).filter(filter));
  }

  private BoolQueryBuilder checkQuery(
      NestedQueryBuilder nfb2,
      RangeQueryBuilder memDobfind,
      NestedQueryBuilder nfb,
      RangeQueryBuilder dateRangeBuilder) {
    BoolQueryBuilder filter = null;
    if (nfb2 != null) {
      if (memDobfind != null) {
        filter =
            QueryBuilders.boolQuery().must(nfb).must(dateRangeBuilder).must(nfb2).must(memDobfind);
      } else {
        filter = QueryBuilders.boolQuery().must(nfb).must(dateRangeBuilder).must(nfb2);
      }
    } else {
      if (memDobfind != null) {
        filter = QueryBuilders.boolQuery().must(nfb).must(dateRangeBuilder).must(memDobfind);
      } else {
        filter = QueryBuilders.boolQuery().must(nfb).must(dateRangeBuilder);
      }
    }

    return filter;
  }

  private NestedQueryBuilder mapNestedBoostQuery(
      ElasticSearchInput esSearchInput, BoolQueryBuilder nested3, InnerHitBuilder innerHitBuilder) {
    NestedQueryBuilder nfb2res = null;

    if (isNotBlank(esSearchInput.getProviderFirstName())
        || isNotBlank(esSearchInput.getProviderLastName())
        || isNotBlank(esSearchInput.getEntyName())
        || isNotBlank(esSearchInput.getTinNumber())) {
      nfb2res =
          QueryBuilders.nestedQuery(PROVIDER_INFO, nested3, ScoreMode.Avg)
              .innerHit(innerHitBuilder);
    }

    return nfb2res;
  }

  private NestedQueryBuilder mapNestedQuery(
      ElasticSearchInput esSearchInput, BoolQueryBuilder nested2, InnerHitBuilder innerHitBuilder) {
    NestedQueryBuilder nfbres = null;
    if (isNotBlank(esSearchInput.getCheckNumber())
        || isNotBlank(esSearchInput.getSeriesDesignator())
        || isNotBlank(esSearchInput.getConsolidateTypeCode())) {
      nfbres =
          QueryBuilders.nestedQuery(PAYMENT_INFO, nested2, ScoreMode.Avg).innerHit(innerHitBuilder);
    }
    return nfbres;
  }

  private RangeQueryBuilder mapDateRange(
      ElasticSearchInput esSearchInput, GatewayUtil gatewayUtil) {

    RangeQueryBuilder dateRangeBuilderRes = null;
    if (isNotBlank(esSearchInput.getFromDate()) && isNotBlank(esSearchInput.getToDate())) {

      Date fromDate = gatewayUtil.toDate(esSearchInput.getFromDate());
      Date toDate = gatewayUtil.toDate(esSearchInput.getToDate());

      Calendar cal = Calendar.getInstance();
      cal.setTime(fromDate);
      cal.set(Calendar.DATE, cal.get(Calendar.DATE));
      fromDate = cal.getTime();

      cal = Calendar.getInstance();
      cal.setTime(toDate);
      cal.set(Calendar.DATE, (cal.get(Calendar.DATE) + 1));
      toDate = cal.getTime();

      dateRangeBuilderRes =
          QueryBuilders.rangeQuery("c_creation_dt")
              .from(gatewayUtil.toDateTimeWithOffset(fromDate.getTime()))
              .to(gatewayUtil.toDateTimeWithOffset(toDate.getTime()))
              .includeUpper(false);
    }
    return dateRangeBuilderRes;
  }

  private void mapNested2PrefixQuery(BoolQueryBuilder nested2, ElasticSearchInput esSearchInput) {

    if (isNotBlank(esSearchInput.getPayId())) {
      nested2.must(
          QueryBuilders.prefixQuery("payment_info.pay_id", toUpperCase(esSearchInput.getPayId())));
    }

    if (isNotBlank(esSearchInput.getCheckNumber())) {
      nested2.must(
          QueryBuilders.prefixQuery(
              "payment_info.chk_trac_nbr", toUpperCase(esSearchInput.getCheckNumber())));
    }

    if (isNotBlank(esSearchInput.getSeriesDesignator())) {
      nested2.must(
          QueryBuilders.prefixQuery(
              "payment_info.srs_desg_id", toUpperCase(esSearchInput.getSeriesDesignator())));
    }
    if (isNotBlank(esSearchInput.getConsolidateTypeCode())) {
      nested2.must(
          QueryBuilders.prefixQuery(
              "payment_info.consol_typ_cd", toUpperCase(esSearchInput.getConsolidateTypeCode())));
    }
  }

  private void mapMultiMatchQuery(BoolQueryBuilder bqb, ElasticSearchInput esSearchInput) {
    if (isNotBlank(esSearchInput.getPartnerDefinedValue1())) {
      String claimDef1 = toUpperCase(esSearchInput.getPartnerDefinedValue1());
      bqb.must(QueryBuilders.multiMatchQuery(claimDef1, C_UDF1, C_UDF2, C_UDF3, C_UDF4, C_UDF5))
          .minimumShouldMatch(1);
    }

    if (isNotBlank(esSearchInput.getPartnerDefinedValue2())) {
      String claimDef2 = toUpperCase(esSearchInput.getPartnerDefinedValue2());
      bqb.must(QueryBuilders.multiMatchQuery(claimDef2, C_UDF1, C_UDF2, C_UDF3, C_UDF4, C_UDF5))
          .minimumShouldMatch(1);
    }

    if (isNotBlank(esSearchInput.getPartnerDefinedValue3())) {
      String claimDef3 = toUpperCase(esSearchInput.getPartnerDefinedValue3());
      bqb.must(QueryBuilders.multiMatchQuery(claimDef3, C_UDF1, C_UDF2, C_UDF3, C_UDF4, C_UDF5))
          .minimumShouldMatch(1);
    }

    if (isNotBlank(esSearchInput.getPartnerDefinedValue4())) {
      String claimDef4 = toUpperCase(esSearchInput.getPartnerDefinedValue4());
      bqb.must(QueryBuilders.multiMatchQuery(claimDef4, C_UDF1, C_UDF2, C_UDF3, C_UDF4, C_UDF5))
          .minimumShouldMatch(1);
    }

    if (isNotBlank(esSearchInput.getPartnerDefinedValue5())) {
      String claimDef5 = toUpperCase(esSearchInput.getPartnerDefinedValue5());
      bqb.must(QueryBuilders.multiMatchQuery(claimDef5, C_UDF1, C_UDF2, C_UDF3, C_UDF4, C_UDF5))
          .minimumShouldMatch(1);
    }
  }


  private RangeQueryBuilder mapMemDobRangeQuery(
      RangeQueryBuilder memDobfind, ElasticSearchInput esSearchInput) {
    DateTime birthDt = null;

    if (isNotBlank(esSearchInput.getMemberDOB())) {
      // 0001-01-01T00:00:00-06:00
      DateFormat df = new SimpleDateFormat(GatewayConstants.DATE_FORMAT_FINAL);
      Date sampDate = null;
      try {
        sampDate = df.parse(esSearchInput.getMemberDOB());
      } catch (ParseException e) {
        logger.error(e.getMessage());
      }

      Calendar cal = Calendar.getInstance();
      cal.setTime(sampDate);
      cal.set(Calendar.DATE, cal.get(Calendar.DATE));
      sampDate = cal.getTime();
      birthDt = new DateTime(sampDate.getTime());
      DateTime birthDt2 = birthDt.plusDays(1);

      memDobfind =
          QueryBuilders.rangeQuery("p_birth_dt").from(birthDt).to(birthDt2).includeUpper(false);
    }

    return memDobfind;
  }

  private void mapNested3PrefixQuery(BoolQueryBuilder nested3, ElasticSearchInput esSearchInput) {
    if (isNotBlank(esSearchInput.getNpiNumber())) {
      nested3.must(
          QueryBuilders.prefixQuery(
              "provider_info.npi_nbr", toUpperCase(esSearchInput.getNpiNumber())));
    }

    if (isNotBlank(esSearchInput.getTinNumber())) {
      nested3.must(
          QueryBuilders.prefixQuery(
              "provider_info.tin_nbr", toUpperCase(esSearchInput.getTinNumber())));
    }
    if (isNotBlank(esSearchInput.getEntyName())) {
      nested3.must(
          QueryBuilders.prefixQuery(
              "provider_info.entity_nm", toUpperCase(esSearchInput.getEntyName())));
    }

    if (isNotBlank(esSearchInput.getProviderFirstName())) {
      nested3.must(
          QueryBuilders.prefixQuery(
              "provider_info.fst_nm", toUpperCase(esSearchInput.getProviderFirstName())));
    }
    if (isNotBlank(esSearchInput.getProviderLastName())) {
      nested3.must(
          QueryBuilders.prefixQuery(
              "provider_info.lst_nm", toUpperCase(esSearchInput.getProviderLastName())));
    }
    if (isNotBlank(esSearchInput.getProviderMidleName())) {
      nested3.must(
          QueryBuilders.prefixQuery(
              "provider_info.midl_nm", toUpperCase(esSearchInput.getProviderMidleName())));
    }
  }

  private void mapPrefixQuery(BoolQueryBuilder bqb, ElasticSearchInput esSearchInput) {
    if (isNotBlank(esSearchInput.getSystemClaimId())) {
      bqb.must(
          QueryBuilders.prefixQuery(
              "c_adjd_sys_clm_id", toUpperCase(esSearchInput.getSystemClaimId())));
    }

    if (isNotBlank(esSearchInput.getClaimNumber())) {
      bqb.must(QueryBuilders.prefixQuery("c_clm_id", toUpperCase(esSearchInput.getClaimNumber())));
    }

    if (isNotBlank(esSearchInput.getMemberFirstName())) {
      bqb.must(
          QueryBuilders.prefixQuery(
              "p_adjd_fst_nm", toUpperCase(esSearchInput.getMemberFirstName())));
    }

    if (isNotBlank(esSearchInput.getMemberMidleName())) {
      bqb.must(
          QueryBuilders.prefixQuery(
              "p_adjd_midl_nm", toUpperCase(esSearchInput.getMemberMidleName())));
    }

    if (isNotBlank(esSearchInput.getMemberLastName())) {
      bqb.must(
          QueryBuilders.prefixQuery(
              "p_adjd_lst_nm", toUpperCase(esSearchInput.getMemberLastName())));
    }

    if (isNotBlank(esSearchInput.getSuffixName())) {
      bqb.must(
          QueryBuilders.prefixQuery(
              "p_adjd_nm_sfx_txt", toUpperCase(esSearchInput.getSuffixName())));
    }

    if (isNotBlank(esSearchInput.getMemId())) {
      bqb.must(QueryBuilders.prefixQuery("p_adjd_ptnt_id", toUpperCase(esSearchInput.getMemId())));
    }
  }

  private void mapStringQuery(BoolQueryBuilder bqb, ElasticSearchInput esSearchInput) {
    if (isNotBlank(esSearchInput.getPartnerDataOwnerGrpId())) {
      bqb.must(
          QueryBuilders.queryStringQuery(
              "c_partner_data_owner_grp_id:"
                  + toUpperCase(esSearchInput.getPartnerDataOwnerGrpId())));
    }

    if (isNotBlank(esSearchInput.getPartnerProcGrpId())) {
      bqb.must(
          QueryBuilders.queryStringQuery(
              "c_partner_proc_grp_id:" + toUpperCase(esSearchInput.getPartnerProcGrpId())));
    }
  }

  private String toUpperCase(String value) {
    if (isNotBlank(value)) {
      return value.trim().toUpperCase();
    }
    return value;
  }
}
