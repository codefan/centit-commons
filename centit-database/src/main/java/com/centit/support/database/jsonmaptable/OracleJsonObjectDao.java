package com.centit.support.database.jsonmaptable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.alibaba.fastjson.JSONArray;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.DatabaseAccess;
import com.centit.support.database.QueryUtils;
import com.centit.support.database.metadata.TableInfo;

public class OracleJsonObjectDao extends GeneralJsonObjectDao {

	public OracleJsonObjectDao(){
		
	}
	
	public OracleJsonObjectDao(Connection conn) {
		super(conn);
	}	
	
	public OracleJsonObjectDao(TableInfo tableInfo) {
		super(tableInfo);
	}
	
	public OracleJsonObjectDao(Connection conn,TableInfo tableInfo) {
		super(conn,tableInfo);
	}
	
	@Override
	public JSONArray listObjectsByProperties(final Map<String, Object> properties,
			final int startPos,final int maxSize)
	throws SQLException, IOException {		
		TableInfo tableInfo = this.getTableInfo();		
		Pair<String,String[]> q = buildFieldSql(tableInfo,null);
		String filter = buildFilterSql(tableInfo,null,properties.keySet());
		String sql = "select " + q.getLeft() +" from " +tableInfo.getTableName();
		if(StringUtils.isNotBlank(filter))
			sql = sql + " where " + filter;	
		return DatabaseAccess.findObjectsByNamedSqlAsJSON(
					getConnect(),
					QueryUtils.buildOracleLimitQuerySQL(
							sql,
							startPos, maxSize, false),
				 properties,
				 q.getRight());
	}
	
	@Override
	public Long getSequenceNextValue(final String sequenceName) throws SQLException, IOException {
		Object object = DatabaseAccess.getScalarObjectQuery(
				 getConnect(),
				 "SELECT " + sequenceName + ".nextval from dual");
		return NumberBaseOpt.castObjectToLong(object);
	}
	
	@Override
	public List<Object[]> findObjectsBySql(final String sSql, final Object[] values, 
			final int pageNo, final int pageSize)
			throws SQLException, IOException {
		int startPos=pageNo>1?(pageNo-1)*pageSize:0;
		return DatabaseAccess.findObjectsBySql(
				getConnect(),
				QueryUtils.buildOracleLimitQuerySQL(
						sSql,
						startPos, pageSize,false),
				values);
	}	

	@Override
	public List<Object[]> findObjectsByNamedSql(final String sSql, final Map<String, Object> values, 
			final int pageNo,final int pageSize) throws SQLException, IOException {
		int startPos=pageNo>1?(pageNo-1)*pageSize:0;
		return DatabaseAccess.findObjectsByNamedSql(
				getConnect(),
				QueryUtils.buildOracleLimitQuerySQL(
						sSql,
						startPos, pageSize,false),
				values);
	}

	@Override
	public JSONArray findObjectsAsJSON(final String sSql, final Object[] values, final String[] fieldnames,
			final int pageNo, final int pageSize)
			throws SQLException, IOException {
		int startPos=pageNo>1?(pageNo-1)*pageSize:0;
		return DatabaseAccess.findObjectsAsJSON(
				getConnect(),
				QueryUtils.buildOracleLimitQuerySQL(
						sSql,
						startPos, pageSize,false),
				values,fieldnames);
	}

	@Override
	public JSONArray findObjectsByNamedSqlAsJSON(final String sSql, final Map<String, Object> values,
			final String[] fieldnames, final int pageNo, final int pageSize) throws SQLException, IOException {
		int startPos=pageNo>1?(pageNo-1)*pageSize:0;
		return DatabaseAccess.findObjectsByNamedSqlAsJSON(
				getConnect(),
				QueryUtils.buildOracleLimitQuerySQL(
						sSql,
						startPos, pageSize,false),
				values,fieldnames);
	}
}
