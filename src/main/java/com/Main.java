package com;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static List<Product> products;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        products = new ArrayList<>();
        System.out.println("Start");

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);

//        readExcelFile(s);


        //        System.out.println("Enter  Digikala Url :");
//
//        String s = in.nextLine();
//        System.out.println("You entered string " + s);
        getProductInfo(s);
//        try {
//            createExcel(s);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        startM3();


    }

    private static void createExcel0(String path) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Java Books");

        Object[][] bookData = {
                {"Head First Java", "Kathy Serria", 79},
                {"Effective Java", "Joshua Bloch", 36},
                {"Clean Code", "Robert martin", 42},
                {"Thinking in Java", "Bruce Eckel", 35},
        };

        int rowCount = 0;

        for (Object[] aBook : bookData) {
            Row row = sheet.createRow(++rowCount);

            int columnCount = 0;

            for (Object field : aBook) {
                Cell cell = row.createCell(++columnCount);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }

        }

        File myFile = new File(path + "/data.xls");
        try (FileOutputStream outputStream = new FileOutputStream(myFile)) {
            workbook.write(outputStream);
        }

    }



    private static void createExcel(String path) throws IOException {
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet("Java Books");

        File empty = new File(path + "/empty.xlsx");
        FileInputStream fis = new FileInputStream(empty);
        XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
        XSSFSheet sheet = myWorkBook.getSheetAt(0);

        try {

            for (int index = sheet.getLastRowNum(); index > sheet.getFirstRowNum(); index--) {
                sheet.removeRow(sheet.getRow(index));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        int rowCount = 0;

        for (Product ps : products) {
            Row row = sheet.createRow(++rowCount);

            int columnCount = 0;
            Cell cellIde = row.createCell(columnCount);
            cellIde.setCellValue("");

            Cell cellType = row.createCell(++columnCount);
            cellType.setCellValue("variable");
            ++columnCount;

            Cell cellTitle = row.createCell(++columnCount);
            cellTitle.setCellValue(ps.title);

            Cell cellIsPublished = row.createCell(++columnCount);
            cellIsPublished.setCellValue("-1");

            Cell cellIsSpecial = row.createCell(++columnCount);
            cellIsSpecial.setCellValue("0");

            Cell cellCatalog = row.createCell(++columnCount);
            cellCatalog.setCellValue("visible");

            ++columnCount;
            Cell cellDes = row.createCell(++columnCount);
            cellDes.setCellValue(ps.des);
            columnCount = 38;

            for (Param field : ps.paramsList) {
                Cell key = row.createCell(++columnCount);
                key.setCellValue(field.key);
                StringBuilder value = new StringBuilder();
                for (String v : field.value) {
                    value.append(v).append("\n");
                }
                Cell valueCell = row.createCell(++columnCount);
                valueCell.setCellValue(value.toString());

                Cell valueCell0 = row.createCell(++columnCount);
                valueCell0.setCellValue(1);

                Cell valueCell00 = row.createCell(++columnCount);
                valueCell00.setCellValue(1);

            }

        }


        File myFile = new File(path + "/data.xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(myFile)) {
            myWorkBook.write(outputStream);
        }

//        FileInputStream datai = new FileInputStream(myFile);
////        XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
////        XSSFSheet sheet = myWorkBook.getSheetAt(0);
//
//        XSSFWorkbook wb = new XSSFWorkbook(datai);
//        DataFormatter formatter = new DataFormatter();
//        File myFileCsv = new File(path + "/data.csv");
//
//        PrintStream out = new PrintStream(new FileOutputStream(myFileCsv),
//                true, "UTF-8");
//
//        for (Sheet sheettt : wb) {
//            for (Row row : sheettt) {
//                boolean firstCell = true;
//                for (Cell cell : row) {
//                    if ( ! firstCell ) out.print(',');
//                    String text = formatter.formatCellValue(cell);
//                    test = text.
//                    out.print(text);
//                    firstCell = false;
//                }
//                out.println();
//            }
//        }

    }

    private static void readExcelFile(String path) {
        File myFile = new File(path + "empty.xlsx");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(1);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void getProductInfo(String path) {
        final boolean[] addingMode = {true};
        Scanner in = new Scanner(System.in);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (addingMode[0]) {
                    try {
                        System.out.println("Start add new product");
                        System.out.println("Enter Digikala Url :");
                        String url = in.nextLine();
//                        String url = "https://www.digikala.com/product/dkp-540389/%D9%85%D8%A7%D8%AF%D8%B1%D8%A8%D8%B1%D8%AF-%D8%A7%DB%8C%D8%B3%D9%88%D8%B3-%D9%85%D8%AF%D9%84-tuf-b360-pro-gaming";
                        Document doc1 = Jsoup.connect(url).get();

                        String title = getTitle(doc1);
                        String des = getDes(doc1);
                        List<Param> params = getParams(doc1);
                        Product product = new Product(title, des, params);
                        products.add(product);

                        System.out.println("Do you want to add new ? (y or n)");
                        String s = in.nextLine();
                        if (s.equals("n")) {
                            addingMode[0] = false;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    createExcel(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private static String getTitle(Document doc1) {
        Elements body = doc1.getElementsByTag("body");
        Elements main = body.get(0).getElementsByTag("main");
        Elements div0 = main.get(0).getElementsByTag("div");
        Elements div1 = div0.get(1).getElementsByTag("div");
        Elements div2 = div1.get(0).getElementsByTag("div");
        Elements article = div2.get(0).getElementsByTag("article");
        Elements section = article.get(0).getElementsByTag("section");
        Elements div3 = section.get(0).getElementsByTag("div");
        Elements div4 = div3.get(0).getElementsByTag("div");
        Elements h1 = div4.get(0).getElementsByTag("h1");
        String title = h1.text();
        System.out.println(String.format("Title is %s", title));

        return title;
    }

    private static String getDes(Document doc1) {
        Element desc = doc1.getElementById("desc");
        Elements article = desc.getElementsByTag("article");
        Elements section = article.get(0).getElementsByTag("section");
        Elements div = section.get(0).getElementsByTag("div");
        Elements div0 = div.get(0).getElementsByTag("div");
        String descc = div0.text();
        System.out.println(String.format("Des is %s", descc));


//        String title = h1.text();
//        System.out.println(String.format("Title is %s", title));

        return descc;
    }

    private static List<Param> getParams(Document doc1) {
        List<Param> params = new ArrayList<>();
        Element div = doc1.getElementById("params");
        Elements article = div.getElementsByTag("article");
        List<Param> par = realParamsFromSection(article.get(0).child(1));

        if (article.get(0).childNodeSize() > 2) {
            Element div0 = article.get(0).child(2);
            Elements sections = div0.getElementsByTag("section");
            for (Element sec : sections) {
                List<Param> p = realParamsFromSection(sec);
                par.addAll(p);
            }
        }
        par.removeIf(param -> param.key.equals(""));

        return par;
    }

    private static List<Param> realParamsFromSection(Element child) {
        Elements ul = child.getElementsByTag("ul");
        Elements lis = ul.get(0).getElementsByTag("li");
        List<String> paramsValues = new ArrayList<>();
        List<Param> params = new ArrayList<>();
        for (Element li : lis) {
            Param param = new Param();
            Elements span0 = li.child(0).getElementsByTag("span");
            Elements span1 = li.child(1).getElementsByTag("span");

            String des = "";
            String title = " . . . . ";

            if (span0.size() != 0) {
                title = span0.get(0).ownText();
                param.setKey(title);
                paramsValues = new ArrayList<>();
            }

            des = span1.get(0).text();
            paramsValues.add(des);
            System.out.println(String.format("com.Param is %s -> %s", title, des));
            param.setValue(paramsValues);
            params.add(param);
        }
        return params;
    }

//
//    private static void startM3() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//
//                        Connection.Response res = Jsoup
//                                .connect("http://192.168.3.113/cgi-bin/luci")
//                                .data("luci_username", "root", "luci_password", "root")
//                                .method(Connection.Method.POST)
//                                .execute();
//
//                        //This will get you cookies
//                        Map<String, String> cookies = res.cookies();
////            System.out.println(cookies);
//                        getSummery(cookies);
//                        getDevices(cookies);
//                        getPools(cookies);
//                        getEvents(cookies);
////                        getDevices(cookies);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        Thread.sleep(20 * 1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//
//    }
//
//    private static void getSummery(Map<String, String> cookies) {
//        long start = System.currentTimeMillis();
//        Document doc1 = null;
//        try {
//            doc1 = Jsoup.connect("http://192.168.3.113/cgi-bin/luci/admin/status/cgminerstatus").cookies(cookies).get();
//            ArrayList<String> titles = new ArrayList<>();
//            ArrayList<String> values = new ArrayList<>();
//            Elements fieldsets = doc1.getElementsByTag("fieldset");
//            Elements legends = fieldsets.get(0).getElementsByTag("legend");
//            System.out.println(legends.text());
//
//            Elements tables = fieldsets.get(0).getElementsByTag("table");
//            showTableData(tables.get(0), start);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private static void getDevices(Map<String, String> cookies) {
//        long start = System.currentTimeMillis();
//        Document doc1 = null;
//        try {
//            doc1 = Jsoup.connect("http://192.168.3.113/cgi-bin/luci/admin/status/cgminerstatus").cookies(cookies).get();
//            ArrayList<String> titles = new ArrayList<>();
//            ArrayList<String> values = new ArrayList<>();
//            Elements fieldsets = doc1.getElementsByTag("fieldset");
//            Elements legends = fieldsets.get(1).getElementsByTag("legend");
//            System.out.println(legends.text());
//
//            Elements tables0 = fieldsets.get(1).getElementsByTag("table");
//            Elements tables1 = fieldsets.get(2).getElementsByTag("table");
//            showTableData(tables0.get(0), start);
//            showTableData(tables1.get(0), start);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private static void getPools(Map<String, String> cookies) {
//        long start = System.currentTimeMillis();
//        Document doc1 = null;
//        try {
//            doc1 = Jsoup.connect("http://192.168.3.113/cgi-bin/luci/admin/status/cgminerstatus").cookies(cookies).get();
//            ArrayList<String> titles = new ArrayList<>();
//            ArrayList<String> values = new ArrayList<>();
//            Elements fieldsets = doc1.getElementsByTag("fieldset");
//            Elements legends = fieldsets.get(3).getElementsByTag("legend");
//            System.out.println(legends.text());
//
//            Elements tables = fieldsets.get(3).getElementsByTag("table");
//            showTableData(tables.get(0), start);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private static void getEvents(Map<String, String> cookies) {
//        long start = System.currentTimeMillis();
//        Document doc1 = null;
//        try {
//            doc1 = Jsoup.connect("http://192.168.3.113/cgi-bin/luci/admin/status/cgminerstatus").cookies(cookies).get();
//
//            Elements fieldsets = doc1.getElementsByTag("fieldset");
//            Elements legends = fieldsets.get(4).getElementsByTag("legend");
//            System.out.println(legends.text());
//
//            Elements tables = fieldsets.get(4).getElementsByTag("table");
//            showTableData(tables.get(0), start);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private static void showTableData(Element table, long start) {
//        ArrayList<String> values = new ArrayList<>();
//        Elements tbody = table.getElementsByTag("tbody");
//        Elements trs = tbody.get(0).getElementsByTag("tr");
//
//        StringBuilder title = new StringBuilder();
//        for (int i = 0; i < trs.size(); i++) {
//            Elements ths = trs.get(i).getElementsByTag("th");
//            if (ths.size() > 0) {
////                    titles = new ArrayList<>();
//                for (int j = 0; j < ths.size(); j++) {
//                    String val = ths.get(j).text();
////                                    System.out.println(val);
////                        titles.add(val);
//                    title.append(" ").append(val);
//                }
//            }
//            StringBuilder value = new StringBuilder();
//            Elements tds = trs.get(i).getElementsByTag("td");
//            if (tds.size() > 0) {
////                    values = new ArrayList<>();
//                for (int k = 0; k < tds.size(); k++) {
//                    String val = tds.get(k).text();
////                                    System.out.println(val);
//                    value.append("    ").append(val);
//                }
//                values.add(value.toString());
//
//            }
//
//        }
//        System.out.println(title);
//
//        for (int j = 0; j < values.size(); j++) {
//            System.out.println(values.get(j));
//        }
//        long finish = System.currentTimeMillis();
//        long timeElapsed = finish - start;
//        System.out.println(" + + + + + + + + + + + timeElapsed = " + timeElapsed / 1000 + "\n \n");
//    }
}
