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
/* 新增商品開始 */

$(document).ready(function() {
	$('form').submit(function(event) {
		event.preventDefault(); // 取消表单提交事件

		Swal.fire({
			title: '確定新增商品？',
			icon: 'question',
			showCancelButton: true,
			confirmButtonText: '新增',
			cancelButtonText: '取消'
		}).then((result) => {
			if (result.isConfirmed) {
				// 最后，如果需要，手动提交表单
				$(this).unbind('submit').submit();
			}
		});
	});
});

/* 新增商品結束 */
/* 預覽圖片開始 */
function previewImages(input) {
	var previewContainer = document.querySelector('.preview'); // 預覽容器
	previewContainer.innerHTML = ""; // 清空預覽容器

	var files = input.files;
	if (files.length > 3) {
		Swal.fire({
			icon: 'error',
			title: 'Oops...',
			text: '只能上傳三張圖片!',
		});
		input.value = ""; // 清空已選檔案
		return;
	}

	for (var i = 0; i < files.length; i++) {
		var file = files[i];
		var reader = new FileReader();

		reader.onload = function(e) {
			var image = document.createElement("img");
			image.src = e.target.result;
			image.classList.add("preview-image");
			image.style.width = "140px";
			image.style.height = "140px";
			previewContainer.appendChild(image);
		};

		reader.readAsDataURL(file);
	}
}
/* 預覽圖片結束 */
/* 清除預覽開始 */
function clearPreview() {
	var previewContainer = document.querySelector('.preview'); // 預覽容器
	previewContainer.innerHTML = "";
}

/* 清除預覽結束 */



