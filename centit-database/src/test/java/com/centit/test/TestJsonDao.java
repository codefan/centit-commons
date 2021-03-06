package com.centit.test;

import java.sql.SQLException;

import com.alibaba.fastjson.JSONObject;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.DbcpConnect;
import com.centit.support.database.DbcpConnectPools;
import com.centit.support.database.ddl.DDLOperations;
import com.centit.support.database.ddl.OracleDDLOperations;
import com.centit.support.database.jsonmaptable.JsonObjectDao;
import com.centit.support.database.jsonmaptable.OracleJsonObjectDao;
import com.centit.support.database.metadata.SimpleTableField;
import com.centit.support.database.metadata.SimpleTableInfo;

public class TestJsonDao {
	
	public  static void  main(String[] args)   {
		 testJDBCMetadata();
	}
  
	public  static void testJDBCMetadata(){
		DataSourceDescription dbc = new DataSourceDescription();	  
		dbc.setConnUrl("jdbc:oracle:thin:@192.168.131.81:1521:orcl");
		dbc.setUsername("metaform");
		dbc.setPassword("metaform");
		try {
			DbcpConnect conn= DbcpConnectPools.getDbcpConnect(dbc);
			SimpleTableInfo tableInfo = new SimpleTableInfo("TEST_TABLE");
			SimpleTableField field = new SimpleTableField();
			field.setColumnName("ID");
			field.setColumnType("Number(10)");
			field.setPrecision(10);
			field.setScale(0);
			field.setMandatory(true);
			field.setPropertyName("id");
			tableInfo.getColumns().add(field);
			
			field = new SimpleTableField();
			field.setColumnName("USER_NAME");
			field.setColumnType("varchar2");
			field.setPropertyName("userName");
			field.setMaxLength(50);
			tableInfo.getColumns().add(field);
			
			field = new SimpleTableField();
			field.setColumnName("USER_PHONE");
			field.setColumnType("varchar2");
			field.setMaxLength(20);
			field.setPropertyName("userPhone");
			field.setDefaultValue("'110'");
			tableInfo.getColumns().add(field);			
			
			tableInfo.getPkColumns().add("ID");
			
			DDLOperations ddl = new OracleDDLOperations(conn);
			ddl.createTable(tableInfo);
			JSONObject object = new JSONObject();
			object.put("id", 1);
			object.put("userName", "codefan");
			//object.put("userPhone", "18602554255");
			
			JsonObjectDao dao = new OracleJsonObjectDao(conn,tableInfo);
			dao.saveNewObject(object);
			conn.commit();
			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
		}
	    System.out.println("done!");
	}	
}
