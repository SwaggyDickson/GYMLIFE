

/*-----------計算總價開始-------------  */
$(document).ready(
	function() {
		// 計算總價格
		updateTotal();

		// 綁定 input 的 change 事件
		$('.itemNum').on(
			'change',
			function() {
				const itemNum = $(this).val();
				const comId = $(this).attr("data-productId");
				console.log("修改數量的comId: "+comId);

				// 更新 hidden 標籤
				$(`#itemNum-` + comId).val(itemNum);

				const itemNums = document
					.querySelectorAll('.itemNum');

				// 透過迴圈取得每個數量欄位的值
				itemNums.forEach(function(itemNum) {
					console.log(itemNum.value);
					console.log($(`#itemNum-${comId}`).val());
					console.log("----- ");
				});

				updateTotal();

			});

	});

function updateTotal() {
	let total = 0;

	$('.itemNum').each(function() {
		const price = parseInt($(this).data('price'), 10);
		const num = parseInt($(this).val(), 10);
		if (!isNaN(num)) {
			total += price * num;
		}
	});

	$('.total').text('$' + total.toFixed(2));
	$('.totalPrice').val(total.toFixed(2));
}
/*-----------計算總價結束-------------  */


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