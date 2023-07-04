package tw.gymlife.course.controller;


import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.QueryTradeInfoObj;

public class test {

	public static String main(String[] args) {
			AllInOne all = new AllInOne("");
			QueryTradeInfoObj obj = new QueryTradeInfoObj();
			obj.setMerchantTradeNo("1b3ab52b983840bfb37c");
			return all.queryTradeInfo(obj);
		}
	}

