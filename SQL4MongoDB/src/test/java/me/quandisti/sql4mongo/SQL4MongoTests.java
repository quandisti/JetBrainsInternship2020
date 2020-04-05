package me.quandisti.sql4mongo;

import org.junit.Assert;
import org.junit.Test;

public class SQL4MongoTests {

    @Test
    public void selectAnythingTest(){
        String inp = "SELECT * FROM database";
        String out = "db.database.find({})";

        Assert.assertEquals(SQL4MongoTranslator.translateQuery(inp), out);
    }

    @Test
    public void selectColumnsTest(){
        String inp = "SELECT name,surname FROM customers";
        String out = "db.customers.find({}, {name: 1, surname: 1})";

        Assert.assertEquals(SQL4MongoTranslator.translateQuery(inp), out);
    }

    @Test
    public void selectColumnsWithOffsetTest(){
        String inp = "SELECT items FROM collection OFFSET 5";
        String out = "db.collection.find({}, {items: 1}).skip(5)";

        Assert.assertEquals(SQL4MongoTranslator.translateQuery(inp), out);
    }

    @Test
    public void selectColumnsWithLimitTest(){
        String inp = "SELECT items, ids FROM collection LIMIT 10";
        String out = "db.collection.find({}, {items: 1, ids: 1}).limit(10)";

        Assert.assertEquals(SQL4MongoTranslator.translateQuery(inp), out);
    }

    @Test
    public void selectColumnsWithOffsetAndLimitTest(){
        String inp1 = "SELECT items, ids FROM collection OFFSET 7 LIMIT 10";
        String inp2 = "SELECT items, ids FROM collection LIMIT 10 OFFSET 7";

        String out = "db.collection.find({}, {items: 1, ids: 1}).skip(7).limit(10)";

        Assert.assertEquals(SQL4MongoTranslator.translateQuery(inp1), out);
        Assert.assertEquals(SQL4MongoTranslator.translateQuery(inp2), out);
    }

    @Test
    public void selectAnythingWithSinglePredicateTest(){
        String inp = "SELECT * FROM customers WHERE age > 22";
        String out = "db.customers.find({age: {$gt: 22}})";

        Assert.assertEquals(SQL4MongoTranslator.translateQuery(inp), out);
    }

    @Test
    public void selectColumnsWithSinglePredicateAndOffsetAndLimitTest(){
        String inp = "SELECT address FROM customers WHERE age < 18 LIMIT 50 OFFSET 50";
        String out = "db.customers.find({age: {$lt: 18}}, {address: 1}).skip(50).limit(50)";

        Assert.assertEquals(SQL4MongoTranslator.translateQuery(inp), out);
    }

    @Test
    public void selectAnythingWithPredicatesAndOffsetAndLimitTest(){
        String inp = "SELECT * FROM customers WHERE age > 22 AND name = 'Vasya' LIMIT 10 OFFSET 20";
        String out = "db.customers.find({age: {$gt: 22}, name: 'Vasya'}).skip(20).limit(10)";

        Assert.assertEquals(SQL4MongoTranslator.translateQuery(inp), out);
    }

}
