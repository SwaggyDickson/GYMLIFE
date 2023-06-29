/*  ---------------------------------------------------
  Template Name: Gym
  Description:  Gym Fitness HTML Template
  Author: Colorlib
  Author URI: https://colorlib.com
  Version: 1.0
  Created: Colorlib
---------------------------------------------------------  */
/*-----------購物車按鈕開始-------------  */
function handleCartLinkClick(event, comId) {
	console.log(comId);
	event.preventDefault(); // 阻止默认的跳转行为

	// 在此触发 SweetAlert 弹跳窗口
	Swal.fire({
		title: '確定加入購物車？',
		text: "",
		icon: 'info',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: '是的！'
	}).then((result) => {
		if (result.isConfirmed) {

		$.ajax({
			url: 'http://localhost:8080/gymlife/userAddCart.func', // 替換成你的controller的URL
			type: 'POST', // 或 'GET'，根據你的需求
			data: {
				itemNumber: 1, // 傳遞使用者在表單中動態更新的數量
				comId: comId // 傳遞商品的comId
			},
			success: function(response) {
				// 在成功接收到回應後執行的程式碼
				console.log(response);
				Swal.fire({
					title: '加入成功!',
					icon: 'success',
					confirmButtonText: '確定'
				})
			},
			error: function(xhr, status, error) {
				// 在發生錯誤時執行的程式碼
				console.error(xhr+" "+status+" "+error);
				Swal.fire({
					title: '發生錯誤，加入失敗!',
					icon: 'error',
					confirmButtonText: '確定'
				})
			}
		});
		}
	});

};
/*-----------購物車按鈕結束-------------  */

/*-----------回到上方按鈕開始-------------  */
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
/*-----------回到上方按鈕結束-------------  */ 
