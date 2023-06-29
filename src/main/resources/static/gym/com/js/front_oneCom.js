/*  ---------------------------------------------------
  Template Name: Gym
  Description:  Gym Fitness HTML Template
  Author: Colorlib
  Author URI: https://colorlib.com
  Version: 1.0
  Created: Colorlib
---------------------------------------------------------  */
/*-----------購物車按鈕開始-------------  */
$(function() {
	$('#addCart').on('click', function(e) {
		e.preventDefault(); // 防止表單直接送出

		var itemNumber = $('#itemNumber').val(); // 取得使用者在表單中動態更新的數量
		var comId = $('input[name="comId"]').val(); // 取得商品的comId
		console.log("數量:" + itemNumber);
		console.log("ID:" + comId);

		$.ajax({
			url: 'http://localhost:8080/gymlife/userAddCart.func', // 替換成你的controller的URL
			type: 'POST', // 或 'GET'，根據你的需求
			data: {
				itemNumber: itemNumber, // 傳遞使用者在表單中動態更新的數量
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
	});


});
/*-----------購物車按鈕結束-------------  */
/*-----------圖片開始-------------  */
$(function() {

})
/*-----------圖片結束-------------  */

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