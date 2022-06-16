![Thanks to the webmagic](http://i.imgur.com/6AZQXjS.jpg)


----------


# TMALL CRAWLER #

> Thanks to the open source webmagic

> This is only a very simple use of webmagic
 
> Whitch get infos from the Tmall 

> Include productID,productName,productPrice,shopName

> **PS:this program is only use for the original tmall shop page ,whitch include food ,drink ,clothes,things using in the life ,they are on "天猫Tmall.com" ,others like electricities are on the different page "天猫电器城" ,it has different path ,so don't use this program on it.**


## FEATURES ##

- simple to use
- easy read --- every codes has note on it


## WARNINGS ##
- need jdk 1.7
- need maven 3 or upper
- need microsoft excel 2007 or upper
- need your network


## HOW TO USE ##
1.	get my files


	![](http://i.imgur.com/haZSCIz.jpg)
2.	Unzip it to a place
   sample : D:\tmallcrawler

	![](http://i.imgur.com/2qyes7C.jpg)

3.	now open your cmd 
   and enter the catalog of the files your Unzip just before
	![](http://i.imgur.com/PjKDiSl.jpg)
4.	use mvn command to compile it
   
	mvn clean compile

	run as following:
	![](http://i.imgur.com/bI82JGb.jpg)
	success!
	
	that means your environment are all right!

	go on!

5.  do
	
	mvn clean test
	
	mvn clean package
	
	mvn clean install

	if you get result like this
	
	![](http://i.imgur.com/OOb5E6Z.jpg)
	
	congratulations!you have done all the things for prepared!

6.	find the *url.txt* in the rootcatalog
	![](http://i.imgur.com/68KMJkZ.jpg)
	if there is not

	create it!

	name: url.txt

	open it
	
	and write down url you want to extract

	here is a example :https://list.tmall.com/search_product.htm?q=%B6%CC%D0%E4&type=p&spm=a220m.1000858.a2227oh.d100&from=.list.pc_1_searchbutton



	**DO REMEBER:you need sign in the tmall.com**

	**otherwise you will not get ANY infomations**

7.  all the things we need do has been done

	now lets extract it!

	open your cmd

	enter the catalog of tmallcrawler

	run the command:

	***java -jar target/tmallcrawler-0.0.1-SNAPSHOT.jar***

	if all the things are right ,you can get info on the sreen
	![](http://i.imgur.com/Hy9KHF5.jpg)
	and the result will also be saved on the file : mall_info.csv

	![](http://i.imgur.com/8qyQqKE.jpg)

	you can open and see it

	![](http://i.imgur.com/J7Alk5s.jpg)

	quit cooooool


## code ##



	public void process(Page page) {
		
		//做一个循环，输出所有页面的商品和商家信息
		for (int i=1;i<=account;i++){
		//天猫有特殊商品，和普通的商品抓取逻辑有区别，此处跳过
		if ((page.getHtml().xpath("//div[@id='J_ItemList']//div["+i+"]/@data-id").toString()==""))
		{continue;}
		//计算当前商家的编号
		int j = i + n*account;
		//抓取商品id
		String idxpath = "//div[@id='J_ItemList']//div["+i+"]//@data-id" ;
		String id = null;
		id = page.getHtml().xpath(idxpath).toString();
		try {
			//调用print方法将信息输出到文件
			print("商品ID",j,id);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//在控制台输出，其实不输出也行
		System.out.println("商品ID"+j+","+id);
		//抓取商品名称
		String productxpath = "//div[@id='J_ItemList']//div["+i+"]//div[@class='product-iWrap']//p[@class='productTitle']//a//@title" ;
		String product = null;
		product = page.getHtml().xpath(productxpath).toString();
		try {
			print("商品名称",j,product);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("商品名称"+j+","+product);
		//抓取商品价格
		String pricexpath = "//div[@id='J_ItemList']//div["+i+"]//div[@class='product-iWrap']//p[@class='productPrice']//em//@title" ;
		String price = null;
		price = page.getHtml().xpath(pricexpath).toString();
		try {
			print("商品价格",j,price);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("商品价格"+j+","+price);
		//抓取卖家名称
		String productShopxpath = "//div[@id='J_ItemList']//div["+i+"]//div[@class='product-iWrap']//div[@class='productShop']//a//text()" ;
		String productShop = null;
		productShop = page.getHtml().xpath(productShopxpath).toString();
		try {
			print("卖家名称",j,productShop);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("卖家名称"+j+","+productShop);
		
		
		}
		
		//抓取下一页的url，并进入，进行下一页的抓取，直到无法跳到下一页
		for(String str: page.getHtml().xpath("////a[@class='ui-page-next']/@href").all()){
			page.addTargetRequest(new Request(str));
			n++;
		}		
	}
	//定义了输出的方法
	public void print (String printname,int num,String value) throws UnsupportedEncodingException, IOException{
		//输出文件为项目根目录下的 mall_info.csv
		OutputStream f = new FileOutputStream("mall_info.csv",true);
		//按照csv的 逗号分隔模式输出
		f.write(printname.getBytes("GBK"));
		f.write(Integer.toString(num).getBytes("GBK"));
		f.write(",".getBytes("GBK"));
		f.write(value.getBytes("GBK"));
		f.write(",".getBytes("GBK"));
		//一个商家输出完毕后换行
		f.write("\r\n".getBytes());
		f.close();
		
	}
	
	//从文件中读取url
	public static String readurl() throws IOException{
		//用Input流读取文件的数据。  
		InputStream in = new FileInputStream("url.txt");

		InputStreamReader isr = new InputStreamReader(in,"gb2312");
		//数组长度暂定为1024
		char []cha = new char[1024];  
		int len = isr.read(cha);  
		System.out.println(new String(cha,0,len)); 
		isr.close();
		String url = new String(cha);
		//System.out.println(url); 
		return url ;
				
	}
	
	public static void main(String[] args) throws IOException {
		//通过readurl()读取url
		String s = readurl();
		//转换成URL格式，否则无法识别
		URL url = new URL (s);
		//爬虫主程序开始
		Spider.create(new testcrawler())
		//此处碰到问题，()要求使用String格式，但是使用String对象传入又不能识别，所以只能在String中嵌入URL
	.addUrl(""+url+"")
	
		.thread(2)
		//启动爬虫
		.run();

	}





## ANY QUESTIONS ##

EMAIL:liuzigenius@126.com
