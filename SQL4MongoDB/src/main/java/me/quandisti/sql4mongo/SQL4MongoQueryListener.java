package me.quandisti.sql4mongo;

import java.util.ArrayList;
import java.util.HashMap;


public class SQL4MongoQueryListener extends SQL4MongoBaseListener {
    public String queryResult = "";

    private String dbName = "";

    private ArrayList<String> findQueries = new ArrayList<String>();
    private HashMap<String, String> queryCharMap = new HashMap<String, String>() {{
        put("=", "$eq");
        put("<>", "$ne");
        put("<", "$lt");
        put(">", "$gt");
    }};

    private ArrayList<String> findProjections = new ArrayList<String>();
    private int limitHowMuch = 0;
    private int skipHowMuch = 0;

    @Override
    public void enterQuery(SQL4MongoParser.QueryContext ctx){
        queryResult = "";
    }
    @Override
    public void exitQuery(SQL4MongoParser.QueryContext ctx){
        StringBuilder result = new StringBuilder();
        result.append("db.");
        result.append(dbName);
        result.append(".find(");
        result.append(findQueries.isEmpty()? "{}" : String.format("{%s}", String.join(", ", findQueries)));
        if (!findProjections.isEmpty())
            result.append(String.format(", {%s}", String.join(", ", findProjections)));
        result.append(")");
        if (skipHowMuch > 0)
            result.append(String.format(".skip(%d)", skipHowMuch));
        if (limitHowMuch > 0)
            result.append(String.format(".limit(%d)", limitHowMuch));
        queryResult = result.toString();
    }

    @Override
    public void enterCols_list(SQL4MongoParser.Cols_listContext ctx){
        findProjections.add(String.format("%s: 1", ctx.column.getText()));
    }

    @Override
    public void exitFrom(SQL4MongoParser.FromContext ctx){
        dbName = ctx.db_name.getText();
    }

    @Override
    public void exitPred(SQL4MongoParser.PredContext ctx){
        if (ctx.operator.getText().equals("="))
            findQueries.add(String.format("%s: %s",
                    ctx.column.getText(),
                    ctx.literal.getText()));
        else
            findQueries.add(String.format("%s: {%s: %s}",
                    ctx.column.getText(),
                    queryCharMap.getOrDefault(ctx.operator.getText(), "$eq"),
                    ctx.literal.getText()));
    }

    @Override
    public void exitLimit(SQL4MongoParser.LimitContext ctx){
        limitHowMuch = Integer.parseInt(ctx.howmuch.getText());
    }

    @Override
    public void exitOffset(SQL4MongoParser.OffsetContext ctx){
        skipHowMuch = Integer.parseInt(ctx.howmuch.getText());
    }

}
