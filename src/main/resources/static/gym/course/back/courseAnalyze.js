//訂單圖表生成
$(document).ready(function() {
	let corderAnalyze = document.getElementById('corderAnalyze');
	corderAnalyze.innerHTML = `<canvas id="courseView"></canvas><canvas id="courseBuyers"></canvas>`;
	viewAnalyze();
	buyerAnalyze();
	});
	
	//觀看人數圖表
	function viewAnalyze(){
//	let corderAnalyze = document.getElementById('corderAnalyze');
//	corderAnalyze.innerHTML = `<canvas id="courseView"></canvas>`;
	axios({
		method: 'get',
		url: 'http://localhost:8080/gymlife/course/corder/analyze'
	})
		.then(res => {
			const view = document.getElementById('courseView');
			let viewName = [];
			let data = [];
			let colors = [];
			for (i = 0; i < res.data.length; i++) {
				viewName.push(res.data[i].courseName);
				data.push(res.data[i].courseViewers);
				//根據資料長度生成不同顏色
				let color = generateColor();
				colors.push(color);
			}
			console.log(data)
			console.log(colors)
			const ViewChat = new Chart(view, {
				type: 'doughnut',
				data: {
					labels: viewName,
					datasets: [{
						label: '觀看人數',
						data: data,
						backgroundColor: colors,
						hoverOffset: 4
					}]
				},
				options: {
					
//					scales: {
//						y: {
//							beginAtZero: true
//						}
//					},
					animation: {
						//						animateRotate:  false,
						animateScale: true

					}, plugins: {
						title: {
							display: true,
							text: '觀看人數',
							font: {
                        size: 20
                    }
						},legend:{
							position:'bottom',font: {
                       
                    },labels: {
                    // This more specific font property overrides the global property
                    font: {
                        size: 18
                    }
                }
						}
					}
				}
			});
	const viewbtn = document.getElementById('viewbtn');
            viewbtn.addEventListener('click', () => {
                const image = ViewChat.toBase64Image();
                const link = document.createElement('a');
                link.href = image;
                link.download = 'Viewchart.png';
                link.click();
            });

		})
};
	//購買人數圖表
	function buyerAnalyze(){
//	let corderAnalyze = document.getElementById('corderAnalyze');
//	corderAnalyze.innerHTML = `<canvas id="courseBuyers"></canvas>`;
	axios({
		method: 'get',
		url: 'http://localhost:8080/gymlife/course/corder/analyze'
	})
		.then(res => {
			const Buyers = document.getElementById('courseBuyers');
			let BuyerscourseName = [];
			let Buyersdata = [];
			let Buyerscolors = [];
			for (i = 0; i < res.data.length; i++) {
				BuyerscourseName.push(res.data[i].courseName)
				Buyersdata.push(res.data[i].courseBuyers);
				//根據資料長度生成不同顏色
				let Buyerscolor = generateColor();
				Buyerscolors.push(Buyerscolor);
			}
			console.log(Buyersdata)
			console.log(Buyerscolors)
			const buyChat = new Chart(Buyers, {
				type: 'doughnut',
				data: {
					labels: BuyerscourseName,
					datasets: [{
						label: '購買人數',
						data: Buyersdata,
						backgroundColor: Buyerscolors,
						hoverOffset: 4
					}]
				},
				options: {
//					scales: {
//						y: {
//							beginAtZero: true
//						}
//					},
					animation: {
						//						animateRotate:  false,
						animateScale: true

					}, plugins: {
						title: {
							display: true,
							text: '購買人數',
							font: {
                        size: 20
                    }
						},legend:{
							position:'bottom',font: {
                       
                    },labels: {
                    // This more specific font property overrides the global property
                    font: {
                        size: 18
                    }
                }
						}
					}
				}
			});
			const buybtn = document.getElementById('buybtn');
            buybtn.addEventListener('click', () => {
                const image = buyChat.toBase64Image();
                const link = document.createElement('a');
                link.href = image;
                link.download = 'Buyerchart.png';
                link.click();
            });


		})
		
};


// 生成顏色的函數
function generateColor() {
	// 隨機生成 RGB 值
	var r = Math.floor(Math.random() * 256);
	var g = Math.floor(Math.random() * 256);
	var b = Math.floor(Math.random() * 256);

	// 將 RGB 值轉換為 CSS 色碼
	var color = 'rgb(' + r + ', ' + g + ', ' + b + ')';

	return color;
}
