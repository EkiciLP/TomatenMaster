package net.Tomatentum.TomatenMaster.database;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.util.List;
//TODO class not finished
@Deprecated
public class Table {

	private String name;
	private Database database;
	private String[] columnSQLs;

	public Table(Database database, String name, String... columnsSQL) {
		this.database = database;
		this.name = name;
		this.columnSQLs = columnsSQL;

		StringBuilder builder = new StringBuilder();
		for (String sql : columnsSQL) {
			builder.append(sql).append(",");
		}

		database.executeUpdate("CREATE TABLE IF NOT EXISTS " + name +
				"(" + builder + ")");
	}

}
