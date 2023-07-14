//訂單圖表生成
$(document).ready(function() {
	viewAnalyze();
//	buyerAnalyze();
	});
	$('#viewbtn').on('click',function(){
		viewAnalyze();
	})
	$('#buybtn').on('click',function(){
		buyerAnalyze();
	})
	$('#buytimebtn').on('click',function(){
		buyTimeAnalyze();
	})
	
	//觀看人數圖表
	function viewAnalyze(){
	let corderAnalyze = document.getElementById('corderAnalyze');
	corderAnalyze.innerHTML = `<canvas id="courseView"></canvas>`;
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
			new Chart(view, {
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

		})
};
	//購買人數圖表
	function buyerAnalyze(){
	let corderAnalyze = document.getElementById('corderAnalyze');
	corderAnalyze.innerHTML = `<canvas id="courseBuyers"></canvas>`;
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
			new Chart(Buyers, {
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

		})
};
	//購買時間圖表
	function getMonthLabels(count) {
  const currentDate = new Date();
  const labels = [];

  for (let i = 0; i < count; i++) {
    const month = currentDate.getMonth();
    const year = currentDate.getFullYear();
    const label = `${year}-${month + 1}`;
    labels.push(label);
    currentDate.setMonth(month - 1);
  }

  return labels.reverse();
}

//const labels = getMonthLabels(7);
//console.log(labels);
	function buyTimeAnalyze(){
	let corderAnalyze = document.getElementById('corderAnalyze');
	corderAnalyze.innerHTML = `<canvas id="courseBuyTime"></canvas>`;
//	axios({
//		method: 'get',
//		url: 'http://localhost:8080/gymlife/course/corder/analyze'
//	})
//		.then(res => {
			const BuyTime = document.getElementById('courseBuyTime');
//			let BuyerscourseName = [];
//			let Buyersdata = [];
//			let Buyerscolors = [];
//			for (i = 0; i < res.data.length; i++) {
//				BuyerscourseName.push(res.data[i].courseName)
//				Buyersdata.push(res.data[i].courseBuyers);
//				//根據資料長度生成不同顏色
//				let Buyerscolor = generateColor();
//				Buyerscolors.push(Buyerscolor);
//			}
//			console.log(Buyersdata)
//			console.log(Buyerscolors)
const labels = getMonthLabels(7);
			new Chart(BuyTime, {
				type: 'line',
				data: {
					 labels: labels,
  datasets: [{
    label: 'My First Dataset',
    data: [65, 59, 80, 81, 56, 55, 40],
    fill: false,
    borderColor: 'rgb(75, 192, 192)',
    tension: 0.1
  }]
				},
//				options: {
////					scales: {
////						y: {
////							beginAtZero: true
////						}
////					},
//					animation: {
//						//						animateRotate:  false,
//						animateScale: true
//
//					}, plugins: {
//						title: {
//							display: true,
//							text: '購買人數',
//							font: {
//                        size: 20
//                    }
//						},legend:{
//							position:'bottom',font: {
//                       
//                    },labels: {
//                    // This more specific font property overrides the global property
//                    font: {
//                        size: 18
//                    }
//                }
//						}
//					}
//				}
			});

//		}
//		)
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
