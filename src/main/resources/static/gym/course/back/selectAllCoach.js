//編輯按鈕隱藏、顯示
$(document).ready(function() {
	$('#edit').on('click', function() {
		console.log($('切換編輯'));
		$('.updatebtn').toggle();
		$('.deletebtn').toggle();
		$('.editth').toggle();
		$('#insertbtn').toggle();
	});
	$('.updatebtn').hide();
	$('.deletebtn').hide();
	$('.editth').hide();
	$('#insertbtn').hide();
})
//編輯按鈕hover
$("#editicon").hover(function() {
	$("#editicon").css("background-color", "#dddddd");
}, function() {
	$("#editicon").css("background-color", "");
});
//新增教練一鍵輸入
$('#fastinsertcoach').on('click', function() {
	$('#ictext1').val("陳筱珮");
	$('#ictext2').val("Candy");
	$('#ictext3').val("0912345678");
	$('#ictext4').val("cindy111@gmail.com");
	$('#ictext5').val("1999-12-12");
	$('#ictext6').val(165);
	$('#ictext7').val(55);
	$('#ictext8').val(`他擁有多年的健身教練經驗，致力於幫助人們達到健康和身材目標。他對於每個學生的需求都非常關注，並以個人化的方法設計適合他們的訓練計劃。他不僅專注於身體的塑造和增強，還注重提升學生的心理素質和自信心。這位男教練以其親切友善的態度和耐心受到學生的喜愛和尊重。他的教學風格充滿活力，能夠激勵和激發學生的潛能。與他一起訓練，你將享受到個人化的指導和專業級的訓練體驗。`);
})
//新增教練取消紐
$('#insertx,#insertx2').on('click', function() {
	$('#ictext1').val("");
	$('#ictext2').val("");
	$('#ictext3').val("");
	$('#ictext4').val("");
	$('#ictext5').val("");
	$('#ictext6').val("");
	$('#ictext7').val("");
	$('#ictext8').val("");
	$('#insertx').attr("data-dismiss", "modal");
});


//更新視窗抓值
function updateCoach(coachId) {
	//用id抓值
	let coachName = document.getElementById('CoachName-' + coachId).innerText;
	let coacheNickName = document.getElementById('CoachNickName-' + coachId).innerText;
	let coachPhoneNumber = document.getElementById('CoachPhoneNumber-' + coachId).innerText;
	let coachEmail = document.getElementById('CoachEmail-' + coachId).innerText;
	let coachBirthday = document.getElementById('CoachBirthday-' + coachId).innerText;
	let coachHeight = document.getElementById('CoachHeight-' + coachId).innerText;
	let coachWeight = document.getElementById('CoachWeight-' + coachId).innerText;
	let coachIntroduce = document.getElementById('CoachIntroduce-' + coachId).innerText;
	//把抓到的值填入更新表單中
	document.getElementById('utext1').value = coachId;
	document.getElementById('utext2').value = coachName;
	document.getElementById('utext3').value = coacheNickName;
	document.getElementById('utext4').value = coachPhoneNumber;
	document.getElementById('utext5').value = coachEmail;
	document.getElementById('utext6').value = coachBirthday;
	document.getElementById('utext7').value = coachHeight;
	document.getElementById('utext8').value = coachWeight;
	document.getElementById('utext9').value = coachIntroduce;
}
//教練圖片Modal
function CoachImg(coachId) {
	//用id抓值
	let coachName = document.getElementById('CoachName-' + coachId).innerText;
	//把抓到的值填入Modal中
	document.getElementById('chtext1').innerText = coachName;
	document.getElementById('changecoachimg').value = coachId;
	//填入照片
	$('#coachimg').attr('src', 'http://localhost:8080/gymlife/coachImage/' + coachId);
}
//教練圖片更新按鈕
$(document).ready(function() {
	//把更換的圖片設成空值
	var originalFile = null;
	//抓取原本圖片的url
	var originalImageURL = document.getElementById('coachimg').src;
	console.log(originalImageURL);
	//按下更新按鈕使用input_file
	document.getElementById('updatechbtn').addEventListener("click", function() {
		document.getElementById('fileInput').click();
	});
	//當更換圖片把更換的圖片存進originalFile
	$('#fileInput').on('change', function(event) {
		originalFile = event.target.files[0];
		// 顯示更換圖片預覽
		var reader = new FileReader();
		reader.onload = function(e) {
			$('#coachimg').attr('src', e.target.result);
		};
		reader.readAsDataURL(originalFile);
		console.log('照片已更換');
		//更換圖片提交按鈕顯示
		$('#CoachImgbtn').show();
	});
	//當按下取消鍵隱藏提交鈕
	$('#cancelButton,#cancelButton2').on('click', function() {
		$('#CoachImgbtn').hide();
		$('#fileInput').val("");
		//清空originalFile
		originalFile = null;
		//回復成原本的圖片
		$('#coachoriginal').attr('src', originalImageURL);
	});
	//初始化隱藏提交鈕
	$('#CoachImgbtn').hide()
});

