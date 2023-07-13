/* 頁面一執行 */
var myChart;
$(document).ready(function() {

	getChatByBuyNumber();
});

/* 回到上方按鈕開始 */
// 當網頁滾動超過 20px 時，按鈕才會顯示
window.onscroll = function() {
	scrollFunction();
};

function scrollFunction() {
	if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
		document.getElementById("back-to-top-btn").classList.remove("hide");
	} else {
		document.getElementById("back-to-top-btn").classList.add("hide");
	}
}

// 按下按鈕後回到頁面頂端
document.getElementById("back-to-top-btn").onclick = function() {
	document.body.scrollTop = 0;
	document.documentElement.scrollTop = 0;
};
/* 回到上方按鈕結束 */

/* DataTable 開始 */

$(document).ready(function() {

	$('.totalSell').each(function() {
		var comPrice = parseInt($(this).closest('tr').find('.comPrice').text());
		var buyNumber = parseInt($(this).closest('tr').find('.buyNumber').text());
		var totalSell = comPrice * buyNumber;
		$(this).text(totalSell);
	});
	$('#myTable').DataTable();
});

/* DataTable 結束 */

/* 圖表功能開始 */

//銷售金額分析
function getCharBySellPrice() {
	if (typeof myChart !== 'undefined') {
		myChart.destroy();
	}

	var title = $('.title');
	title.text("#銷售金額分析");

	$.ajax({
		url: 'http://localhost:8080/gymlife/getChatByComBuyNumber.func', // 替換成你的controller的URL
		type: 'GET', // 或 'GET'，根據你的需求
		dataType: "json",
		success: function(response) {
			console.log(response);

			var labels = []; // 商品标签数组
			var data = []; // 商品数量数组
			var chartOptions = {
				backgroundColor: 'white',
			};
			// 将商品数据转换为适用于Chart.js的格式
			for (var i = 0; i < 5; i++) {
				var product = response[i];
				labels.push(product.comName); // 将商品名称添加到标签数组中
				data.push(product.comBuyNumber * product.comPrice); // 将商品数量添加到数据数组中
			}

			var ctx = document.getElementById('myChart-4');

			var chartData = {
				labels: labels,
				datasets: [{
					label: 'My First Dataset',
					data: data,
					backgroundColor: [
						'rgb(255, 0, 0)',
						'rgb(255, 97, 176)',
						'rgb(255, 66, 255)',
						'rgb(0, 127, 255)',
						'rgb(0, 235, 0)',
						// 添加更多颜色...
					],
					hoverOffset: 4
				}]
			};

			myChart = new Chart(ctx, {
				type: 'pie',
				data: chartData,
				
				options: {
					chartOptions,
					legend: {
						labels: {
							fontColor: 'black' // 标签颜色
						}
					}
				}
			});
		}
		,
		error: function(xhr, status, error) {
			// 在發生錯誤時執行的程式碼
			console.error(xhr + " " + status + " " + error);

		}
	});

}

//購買人數
function getChatByBuyNumber() {

	if (typeof myChart !== 'undefined') {
		myChart.destroy();
	}

	var title = $('.title');
	title.text("#商品購買分析");
	$.ajax({
		url: 'http://localhost:8080/gymlife/getChatByComBuyNumber.func', // 替換成你的controller的URL
		type: 'GET', // 或 'GET'，根據你的需求
		dataType: "json",
		success: function(response) {
			console.log(response);

			var labels = []; // 商品标签数组
			var data = []; // 商品数量数组
			var chartOptions = {
				backgroundColor: 'white',
			};
			// 将商品数据转换为适用于Chart.js的格式
			for (var i = 0; i < 5; i++) {
				var product = response[i];
				labels.push(product.comName); // 将商品名称添加到标签数组中
				data.push(product.comBuyNumber); // 将商品数量添加到数据数组中
			}

			var ctx = document.getElementById('myChart-4');

			var chartData = {
				labels: labels,
				datasets: [{
					label: 'My First Dataset',
					data: data,
					backgroundColor: [
						'rgb(255, 0, 0)',
						'rgb(255, 97, 176)',
						'rgb(255, 66, 255)',
						'rgb(0, 127, 255)',
						'rgb(0, 235, 0)',
						// 添加更多颜色...
					],
					hoverOffset: 4
				}]
			};

			myChart = new Chart(ctx, {
				type: 'pie',
				data: chartData,
				options: {
					chartOptions,
					legend: {
						labels: {
							fontColor: 'black' // 标签颜色
						}
					}
				}
			});
		}
		,
		error: function(xhr, status, error) {
			// 在發生錯誤時執行的程式碼
			console.error(xhr + " " + status + " " + error);

		}
	});
}
//觀看次數
function getCharByClickTime() {

	if (typeof myChart !== 'undefined') {
		myChart.destroy();
	}
	//createChar();
	var title = $('.title');
	title.text("#瀏覽次數分析");
	$.ajax({
		url: 'http://localhost:8080/gymlife/getChatByClickTime.func', // 替換成你的controller的URL
		type: 'GET', // 或 'GET'，根據你的需求
		dataType: "json",
		success: function(response) {
			console.log(response);

			var labels = []; // 商品标签数组
			var data = []; // 商品数量数组
			var chartOptions = {
				backgroundColor: 'white',
			};
			// 将商品数据转换为适用于Chart.js的格式
			for (var i = 0; i < 5; i++) {
				var product = response[i];
				labels.push(product.comName); // 将商品名称添加到标签数组中
				data.push(product.clickTime); // 将商品数量添加到数据数组中
			}

			var ctx = document.getElementById('myChart-4');

			var chartData = {
				labels: labels,
				datasets: [{
					label: 'My First Dataset',
					data: data,
					backgroundColor: [
						'rgb(255, 0, 0)',
						'rgb(255, 97, 176)',
						'rgb(255, 66, 255)',
						'rgb(0, 127, 255)',
						'rgb(0, 235, 0)',
						// 添加更多颜色...
					],
					hoverOffset: 4
				}]
			};

			myChart = new Chart(ctx, {
				type: 'pie',
				data: chartData,
				options: {
					chartOptions,
					legend: {
						labels: {
							fontColor: 'black' // 标签颜色
						}
					}
				}
			});
		}
		,
		error: function(xhr, status, error) {
			// 在發生錯誤時執行的程式碼
			console.error(xhr + " " + status + " " + error);

		}
	});
}
//輸出成圖片
function importImg() {
	var chartCanvas = document.getElementById('myChart-4');
	var image = chartCanvas.toDataURL('image/png');
	$.ajax({
		url: 'http://localhost:8080/gymlife/importImg.func',
		type: 'POST',
		data: { image: image },
		success: function(response) {
			console.log('图像已成功保存到本地。');
		},
		error: function(error) {
			console.log('保存图像时发生错误。');
		}
	});
}


function removeBox() {
	var container = document.getElementById('myDiv'); // 假设有一个id为'container'的容器元素

	// 移除元素
	var colDiv = document.querySelector('.col-xs-12.col-sm-12.col-md-12.col-lg-6.col-xl-6.myChart');
	container.removeChild(colDiv);
}

/* 圖表功能結束 */