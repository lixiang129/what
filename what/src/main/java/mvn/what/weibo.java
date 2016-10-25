package mvn.what;

import java.io.FileReader;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.gson.Gson;

public class weibo {

	public static void main(String[] args) {
		boolean flag = true;
		int count = 0;
		String in = "";
		String bq1, bq2;
		Gson g = new Gson();
		String website = "",content = "";
		int counts = 0;
		int waitTime = 0;
		String [] login = null;
		try (FileReader r = new FileReader(System.getProperty("user.dir")+"/test.json")) {
			B b = g.fromJson(r, B.class);
			website = b.website;
			content = b.content;
			counts = b.counts;
			waitTime = b.waitTime;
			login = b.login;
/*			System.out.println(b.website);
			System.out.println(b.content);
			System.out.println(b.waitTime);
			System.out.println(Arrays.asList(b.login));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Random random = new Random();
		String[] biaoqing = { "[doge]", "[嘻嘻]", "[哈哈]", "[可爱]", "[可怜]",
				"[吃惊]", "[害羞]", "[右哼哼]", "[抱抱_旧]", "[色]", "[爱你]", "[坏笑]",
				"[哆啦A梦吃惊]" };
		System.out.println("Hello World!");
		System.setProperty(
				"webdriver.chrome.driver",
				System.getProperty("user.dir")+"/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			driver.get("http://weibo.com/");
		} catch (Exception ex) {
			System.out.println("页面加载超时，已自动停止");
			((JavascriptExecutor) driver).executeScript("window.stop()");
		}

		while (in.equals("")) {
			String userPassword = login[count];
			String temp [] = userPassword.split("#");
			// 输入用户名和密码
			driver.findElement(By.id("loginname")).clear();
			driver.findElement(By.id("loginname")).sendKeys(temp[0]);
			driver.findElement(By.name("password")).sendKeys(temp[1]);

			try{
				driver.findElement(By.xpath("//a[@node-type='submitBtn']/span[text()='登录']")).click();
				Thread.sleep(2000);
				while(flag){
				try{
					driver.findElement(By.xpath("//a[@node-type='submitBtn']/span[text()='登录']"));
					// 因无法识别验证码，需给与之间手动输入
					Scanner s = new Scanner(System.in);
					System.out.println("请到网页输入验证码,后敲回车继续。。。。。请不要点击登录！！。");
					String code = s.nextLine();
					driver.findElement(By.xpath("//input[@value='验证码']")).sendKeys(code);
					driver.findElement(By.xpath("//a[@node-type='submitBtn']/span[text()='登录']")).click();
					Thread.sleep(2000);
				}catch(NoSuchElementException ex){
					System.out.println("登录成功！！！");
					flag = false;
				}
				}
				((JavascriptExecutor) driver).executeScript("window.stop()");
			}catch(Exception ex){
				((JavascriptExecutor) driver).executeScript("window.stop()");
			}
			// 防止网页加载速度慢停止加载

			// 登录完成后跳转到转发页面并超过3秒停止加载
			try {
				driver.get(website);
			} catch (Exception ex) {
				System.out.println("页面加载超时，已自动停止");
				((JavascriptExecutor) driver).executeScript("window.stop()");
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 点击转发
			driver.findElement(By.xpath("(//div[@class='WB_handle']/ul/li)[2]"))
					.click();
			// 获取转发文字
/*			String text = driver.findElement(
					By.xpath("//div[@class='p_input p_textarea']/textarea"))
					.getText();*/
			String text = content;
			System.out.println(text);
			// 转发文本输入转发文字并变换表情
			for (int i = 0; i < counts; i++) {
/*				listBiaoQing = new ArrayList<String>(Arrays.asList(biaoqing));
				int r = random.nextInt(listBiaoQing.size() - 1);
				bq1 = (String) listBiaoQing.get(r);
				listBiaoQing.remove(i);
				int r2 = random.nextInt(listBiaoQing.size() - 1);
				bq2 = (String) listBiaoQing.get(r);*/

				bq1 = biaoqing[i/10];
				bq2 = biaoqing[i%10];
				System.out.println(bq1 + "-" +bq2);
				// driver.findElement(By.id("ipt11")).click();
				if (i == 0) {
					driver.findElement(
							By.xpath("//div[@class='p_input p_textarea']/textarea"))
							.sendKeys(bq1 + bq2);
				} else {
					driver.findElement(
							By.xpath("//div[@class='p_input p_textarea']/textarea"))
							.sendKeys(bq1 + bq2 + text);
				}
				driver.findElement(
						By.xpath("//a[text()='转发' and @node-type='submit']"))
						.click();
				try {
					Thread.sleep(waitTime*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			driver.findElement(
					By.xpath("//em[@class='W_ficon ficon_set S_ficon']"))
					.click();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driver.findElement(By.xpath("//a[text()='退出']")).click();
			count++;
			flag = true;
		}
		// driver.findElement(By.xpath("(//div[@class='WB_handle']/ul/li)[2]")).click();
		driver.quit();
	}

}
