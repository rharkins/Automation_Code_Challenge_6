package com.stgconsulting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by Richard Harkins on 8/22/2016.
 */

public class WebCrawlerBaseMultiThreaded extends SeleniumWebdriverBaseClass
{
    private static int threadCount = 5;
//    ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);

    public WebCrawlerBaseMultiThreaded() throws IOException {
    }


@Test
    public static void startCrawler() throws InterruptedException, ExecutionException, IOException {
        crawlSite(baseWebPageURL);
    }

    private static void crawlSite(String initialUrl) throws IOException, ExecutionException, InterruptedException {
        LocalDateTime crawlerStartTime = LocalDateTime.now();
        List<WebElement> anchorTags = null;
        List<String> hrefAttributeValues = new ArrayList<String>();
        Queue<String> pagesToVisit = new LinkedList<String>();
        List<String> pagesVisited = new ArrayList<String>();
        String currentPageURL = baseWebPageURL;
//        pagesVisited.add(baseWebPageURL);
        System.out.println("WebCrawler Start Time - " + crawlerStartTime);
        System.out.println("pagesVisited size = " + pagesVisited.size());
//        System.out.println("pagesVisited website = " + pagesVisited.get(pagesVisited.size()-1));

//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') and not(contains(., '@@')) and not(contains(., '?'))]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') and not(contains(., '@@')) and not(contains(., 'blog')) and not(contains(., '?'))]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') or starts-with(.,'/')]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'/')]]"));
        // Get all href attribute values on current page
//        for (WebElement anchorTagElement : anchorTags)
//        {
//            hrefAttributeValues.add(anchorTagElement.getAttribute("href"));
//        }
//        // Check that the href values have not been previously visited
//        for (String hrefValues : hrefAttributeValues)
//        {
//            if (!pagesVisited.contains(hrefValues) && !pagesToVisit.contains(hrefValues))
//            {
//                pagesToVisit.add(hrefValues);
//                System.out.println(hrefValues);
//            }
//        }

        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
        WebPageCrawler crawlPage = new WebPageCrawler(initialUrl);

        ConcurrentLinkedQueue<Future<List<String>>> futures = new ConcurrentLinkedQueue();

        pagesVisited.add(initialUrl);
        futures.add(threadPool.submit(crawlPage));
        while (!futures.isEmpty())
        {
            List<Future<List<String>>> completedFutures = new ArrayList();
            for (Future<List<String>> future : futures)
            {
                if (future.isDone())
                {
                    List<String> newUrls = future.get();
                    for (String newUrl : newUrls)
                    {
                        if (!pagesToVisit.contains(newUrl) && !pagesVisited.contains(newUrl) && newUrl.contains(initialUrl))
                        {
                            System.out.println("New URL found: " + newUrl);
                            pagesToVisit.add(newUrl);
                        }
                    }
                    completedFutures.add(future);
                }
            }
            System.out.println("Visited URLs: " + pagesVisited.size());
            System.out.println("URLs to visit (remaining futures): " + futures.size());
            futures.removeAll(completedFutures);
            while (!pagesToVisit.isEmpty())
            {
                String urlToCrawl = pagesToVisit.poll();
                futures.add(threadPool.submit(new WebPageCrawler(urlToCrawl)));
                pagesVisited.add(urlToCrawl);
            }
            Thread.sleep(500);
        }

//        System.out.println(pagesToVisit.size() + " - pages left to visit");
//        System.out.println("--------------------");
//        // Remove current page from pagesToVisit List
//        pagesToVisit.remove(currentPageURL);
//        // Loop through pagesToVisit List until list is empty
//        while (pagesToVisit.size() > 0)
//        // Go to next page in Pages to visit list
//        {
//            anchorTags.clear();
//            driver.get(pagesToVisit.get(0));
//            pagesVisited.add(pagesToVisit.get(0));
//            System.out.println("pagesVisited size = " + pagesVisited.size());
//            System.out.println("pagesVisited website = " + pagesVisited.get(pagesVisited.size()-1));
//            currentPageURL = pagesToVisit.get(0);
//            anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') and not(contains(., '@@')) and not(contains(., '?'))]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') and not(contains(., '@@')) and not(contains(., 'blog')) and not(contains(., '?'))]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') or starts-with(.,'/')]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'/')]]"));
//            // Get all href attribute values on current page
//            for (WebElement anchorTagElement : anchorTags)
//            {
//                hrefAttributeValues.add(anchorTagElement.getAttribute("href"));
//            }
//            // Check that the href values have not been previously visited
//            for (String hrefValues : hrefAttributeValues)
//            {
//                if (!pagesVisited.contains(hrefValues) && !pagesToVisit.contains(hrefValues))
//                {
//                    pagesToVisit.add(hrefValues);
//                    System.out.println(hrefValues);
//                }
//            }
//            System.out.println(pagesToVisit.size() + " - pages left to visit");
//            System.out.println("--------------------");
//            // Remove current page from pagesToVisit List
//            pagesToVisit.remove(currentPageURL);
//            // Clear out hrefAttributeValues list
//            hrefAttributeValues.clear();
//
//        }
//
//        System.out.println(pagesVisited.size());
        LocalDateTime crawlerEndTime = LocalDateTime.now();
        System.out.println("WebCrawler End Time - " + crawlerEndTime);
        Duration elapsedCrawlerTime = Duration.between(crawlerStartTime, crawlerEndTime);
        long elapsedCrawlerTimeInMinutes = elapsedCrawlerTime.toMinutes();
        long elapsedCrawlerTimeInHours = elapsedCrawlerTime.toHours();
        Duration elapsedCrawlerTimeRemainingMinutesDuration = elapsedCrawlerTime.minusHours(elapsedCrawlerTimeInHours);
        long elapsedCrawlerTimeRemainingMinutes = elapsedCrawlerTimeRemainingMinutesDuration.toMinutes();
        if (elapsedCrawlerTimeRemainingMinutes == 1)
        {
            System.out.println("Total WebCrawler Elapsed Time - " + elapsedCrawlerTimeInHours + " hours" + " " + elapsedCrawlerTimeRemainingMinutes + " minute");
        }
        else
        {
            System.out.println("Total WebCrawler Elapsed Time - " + elapsedCrawlerTimeInHours + " hours" + " " + elapsedCrawlerTimeRemainingMinutes + " minutes");

        }

    }
}
