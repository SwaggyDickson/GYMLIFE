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

/* 上下架狀態讓卡片明顯開始 */
// 获取所有卡片元素
const cardElements = document.querySelectorAll('.card.com-list');

// 遍历每个卡片元素
cardElements.forEach(cardElement => {
	// 获取当前卡片的商品状态
	const comStatusElement = cardElement.querySelector('select[name="comStatus"]');
	const comStatus = comStatusElement.value;

	// 根据商品状态设置卡片主体颜色
	if (comStatus === '下架') {
		cardElement.style.backgroundColor = '#9D9D9D';
	}
});
/* 上下架狀態讓卡片明顯結束 */

/* 一鍵上下架開始 */
// 獲取開關元素
const switchElements = document.querySelectorAll('input[type="checkbox"].hidden-checkbox');

// 監聽開關的點擊事件
switchElements.forEach(function(switchElement) {
	switchElement.onclick = function() {
		// 獲取當前開關狀態
		const isChecked = switchElement.checked;

		// 獲取開關元素的ID
		const comId = switchElement.getAttribute('data-comid');
		console.log(comId);

		// 彈出確認對話框
		Swal.fire({
			title: '確認操作',
			text: isChecked ? '確定上架？' : '確定下架？',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonText: '確認',
			cancelButtonText: '取消'
		}).then(function(result) {
			if (result.isConfirmed) {
				//按下確認後
				var xhr = new XMLHttpRequest();
				xhr.open('PUT', 'changeStatus.func', true);

				// 設置請求標頭
				xhr.setRequestHeader('Content-Type', 'application/json');

				// 設置請求內容
				const requestData = {
					comId: comId,
					isChecked: isChecked
				};
				xhr.send(JSON.stringify(requestData));

				// 處理回覆的響應
				xhr.onreadystatechange = function() {
					if (xhr.readyState === XMLHttpRequest.DONE) {
						if (xhr.status === 200) {
							// 請求成功
							console.log(xhr.responseText);
							switchElement.checked = isChecked;
							location.reload(); // 刷新頁面
						} else {
							// 請求失敗
							console.error('請求失敗');
							switchElement.checked = !isChecked;
						}
					}
				};
			} else {
				// 如果取消確認，將開關復原成原本狀態
				switchElement.checked = !isChecked;
			}
		});
	};
});
/* 一鍵上下架結束 */

/* 修改商品資訊開始 */
function saveChanges(button) {
	var comId = button.getAttribute("id").split("-")[1];
	console.log(comId);
	var comName = document.getElementById("comNameAjax-" + comId).value;
	var comNumber = document.getElementById("comNumberAjax-" + comId).value;
	var comPrice = document.getElementById("comPriceAjax-" + comId).value;
	var comType = document.getElementById("comTypeAjax-" + comId).value;
	var comStatus = document.getElementById("comStatusAjax-" + comId).value;
	var comContent = document.getElementById("comContentAjax-" + comId).value;

	Swal.fire({
		title: '確定要修改嗎？',
		text: "此操作將無法還原！",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: '是的，修改它！'
	}).then((result) => {
		if (result.isConfirmed) {
			// 將值傳遞給 Servlet
			var formData = new FormData();
			formData.append("comId", comId);
			formData.append("comNameAjax", comName);
			formData.append("comNumberAjax", comNumber);
			formData.append("comPriceAjax", comPrice);
			formData.append("comTypeAjax", comType);
			formData.append("comStatusAjax", comStatus);
			formData.append("comContentAjax", comContent);

			var xhr = new XMLHttpRequest();
			xhr.open('PUT', 'editItemInfo.func', true);
			xhr.onreadystatechange = function() {
				if (xhr.readyState === 4) {
					if (xhr.status === 200) {
						// 請在需要時處理回應
						Swal.fire({
							title: '修改成功!',
							icon: 'success',
							confirmButtonText: '確定'
						}).then((result) => {
							if (result.isConfirmed) {
								location.reload(); // 刷新頁面
							}
						});
					} else {
						console.log('修改失敗 - 狀態碼: ' + xhr.status + ', 錯誤訊息: ' + xhr.statusText);
					}
				}
			};
			xhr.send(formData);
		}
	});
}
/* 修改商品資訊結束 */
/* 修改圖片開始 */
document.addEventListener('DOMContentLoaded', function() {

	var imgBtns = document.getElementsByClassName('imgBtn');

	for (var i = 0; i < imgBtns.length; i++) {
		imgBtns[i].addEventListener('click', function() {
			var imageId = this.getAttribute('data-imgId'); // 獲取存在 data-imgId 屬性中的值
			var comId = this.getAttribute('data-comId'); // 獲取存在 data-imgId 屬性中的值


			var input = document.createElement('input');
			input.type = 'file';

			input.addEventListener('change', function() {
				var file = input.files[0];

				var formData = new FormData();

				formData.append('comId', comId);
				formData.append('imageId', imageId);
				formData.append('imageFile', file);

				var xhr = new XMLHttpRequest();

				xhr.onreadystatechange = function() {
					if (xhr.readyState === 4 && xhr.status === 200) {
						console.log('圖片上傳成功');
						location.reload(); // 刷新頁面
					} else if (xhr.readyState === 4 && xhr.status !== 200) {
						console.log('圖片上傳失敗');
					}
				};

				xhr.open('PUT', 'editOnePic.func', true);
				xhr.send(formData);
			});

			input.click();
		});
	}
});
/* 修改圖片結束 */

/* 停止輪播開始 */

$('.carousel').carousel({
  interval: false,
  pause: false
})
/* 停止輪播結束 */


