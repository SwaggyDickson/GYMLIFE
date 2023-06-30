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
					console.error(xhr + " " + status + " " + error);
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
    $('.set-bg').each(function () {
        var bg = $(this).data('setbg');
        $(this).css('background-image', 'url(' + bg + ')');
    });
/* 搜尋按鈕開始 */

$('#searchByName').on('keyup', function() {
	const keyword = $('#keywords').val(); // 获取关键字输入框的值

	console.log(keyword);
	const row = $(".rowcontent");
	const templates = $('.returnList');
	for (var i = 0; i < templates.length; i++) {
		templates[i].innerHTML = "";
	}

	$.ajax({
		url: 'http://localhost:8080/gymlife/userCheckByKeyWord.fun', // 替換成你的controller的URL
		type: 'GET',
		data: {
			keywords: keyword // 将关键字作为参数传递给服务器
		},
		dataType: 'json',
		success: function(response) {
			// 在成功接收到回應後執行的程式碼
			// 选择要插入内容的容器元素
			$(".returnList").remove();
			console.log(response);
			//let result= JSON.parse(response);
			//console.log(result);
			response.forEach(function(response){
				
				var comPicInfo = response.comPicInfo;
				var imageId = Object.keys(comPicInfo)[0];
				// 创建HTML元素并设置内容
				var div = document.createElement('div');
				div.className = 'returnList';
				div.id = 'returnList-' + response.comId;
				div.setAttribute('data-comId', response.comId);
				div.innerHTML = '<div class="col-lg-4 col-sm-6" style="flex: 100%; max-width: 100%;">' +
					'<div class="ts-item set-bg picControl" style="display:flex;align-items:center;">' +
					'<img src="http://localhost:8080/gymlife/download/' +imageId + '">' +
					'<div class="ts_text">' +
					'<h4>' + response.comName + '</h4>' +
					'<span>$' + response.comPrice + '</span>' +
					'<div class="tt_social">' +
					'<a href="#" id="cartLink-' + response.comId + '" class="cart-link" onclick="handleCartLinkClick(event,' + response.comId + ')">' +
					'<i class="fa fa-shopping-cart fa-3x"></i></a>' +
					'<a href="http://localhost:8080/gymlife/com/' + response.comId + '"><i class="fa fa-search fa-3x"></i></a>' +
					'</div></div></div></div>';

				// 将元素插入容器
				row.append(div);
			})
			// 循环遍历comList
			

		},
		error: function(xhr, status, error) {
			// 在發生錯誤時執行的程式碼
			console.error(xhr + " " + status + " " + error);
		}
	});
});

/* 搜尋按鈕結束 */

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
