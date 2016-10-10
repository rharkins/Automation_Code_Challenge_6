package com.stgconsulting;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Richard Harkins on 8/22/2016.
 */
public class WebPageCrawler extends SeleniumWebdriverBaseClass implements Callable
{

    private String url;

    public WebPageCrawler(String url) throws IOException
    {
        this.url = url;
    }

    public List<String> call() throws StaleElementReferenceException
    {
//        LocalDateTime crawlerStartTime = LocalDateTime.now();
        List<WebElement> anchorTags = null;
        List<String> hrefAttributeValues = new ArrayList<String>();
//        List<String> pagesToVisit = new ArrayList<String>();
//        List<String> pagesVisited = new ArrayList<String>();
//        String currentPageURL = baseWebPageURL;
//        pagesVisited.add(baseWebPageURL);
//        System.out.println("WebCrawler Start Time - " + crawlerStartTime);
//        System.out.println("pagesVisited size = " + pagesVisited.size());
//        System.out.println("pagesVisited website = " + pagesVisited.get(pagesVisited.size()-1));

//        anchorTags.clear();
//        hrefAttributeValues.clear();

        driver.navigate().to(url);

        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') and not(contains(., '@@')) and not(contains(., '?'))]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') and not(contains(., '@@')) and not(contains(., 'blog')) and not(contains(., '?'))]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') or starts-with(.,'/')]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'/')]]"));
        // Get all href attribute values on current page

        try
        {

            for (WebElement anchorTagElement : anchorTags) {
                hrefAttributeValues.add(anchorTagElement.getAttribute("href"));
            }

//            return hrefAttributeValues;

        }

        catch(StaleElementReferenceException e)
        {

        }

        catch(NullPointerException e)
        {

        }

        return hrefAttributeValues;
    }
}
