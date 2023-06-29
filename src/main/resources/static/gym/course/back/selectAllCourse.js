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

//新增課程教練選單
const coachSelect = document.getElementById('coachSelect');
coachSelect.addEventListener('change', function() {
	const selectedCoachId = coachSelect.value;
	console.log(selectedCoachId);
	$('#chinsert').val(selectedCoachId);

});

//新增課程一鍵輸入
$('#fastinsertcourse').on('click', function() {
	$('#itext1').val("重量訓練器材");
	$('#itext2').val(3000);
	$('#itext3').val(`軌道固定
固定式機器通常軌道是固定的,比自由重量較適合初學者使用，為什麼呢？ 因為，固定式機械式器材它的設計上只需要依照個人的身型，作適當的調整，在坐姿、臥姿、俯姿的訓練動作上，提供訓練者身體的接觸較多的面積，來幫助訓練時的穩定性，介此在訓練的過程中，降低身體的代償，減少不必要的訓練傷害，固定的移動軌道，你只需要依照個人的訓練強度，做重量插槽的調整，了解這台機器所要訓練的特定肌群部位，操作過程符合力線方向(固定的移動軌道)，感受訓練的目標肌群、保持呼吸(放鬆時吸氣用力吐氣)及隨時保持身體的穩定，便能提供較好及安全的訓練品質。
安全性
還有固定式機械式器材幾乎都設有安全裝置，所以，能降低訓練中發生危險的機率，比較適合健身房初學者使用，固定式機械器材的訓練還有一個非常重要的優點，在於你所使用的健身器材都是經過多年研發與專業認證所設計的，所以更符合人體工學, 對於運動強度和幅度的部分也更為精準，讓你較快速的掌握標準動作， 也更能提升目標肌群的訓練效益 。`);
})
$('#chinsert').val(1);
//新增課程取消紐
$('#insertx,#insertx2').on('click', function() {
	$('#itext1').val("");
	$('#itext2').val("");
	$('#itext3').val("");
	$('#chinsert').val("");
	$('#insertx').attr("data-dismiss", "modal");
});

//刪除課程圖片hover
function deletehover() {
	$('.cimghover').css({
		'position': 'absolute',
		'top': '0',
		'left': '0',
		'width': '100%',
		'height': '100%',
		'background-color': 'rgba(0, 0, 0, 0.5)',
		'color': 'white',
		'font-size': '50px',
		'display': 'flex',
		'justify-content': 'center',
		'align-items': 'center'
	});
	$('.cimg').hover(function() {
		$('.cimghover').show();
	}, function() {
		$('.cimghover').hide();
	});

	$('.cimghover').hide();
}
//新增課程圖片按鈕點選
$('#insertcbtn').on("click", function() {
	$('#insertfileInput').click();
});


//關閉自動輪播
$('.carousel').carousel({
	interval: false
})
//更新視窗抓值
function updateCourse(courseId) {
	//用id抓值
	let courseName = document.getElementById('CourseName-' + courseId).innerText;
	let courseCost = document.getElementById('CourseCost-' + courseId).innerText;
	let courseIntroduce = document.getElementById('CourseIntroduce-' + courseId).innerText;
	let coachId = document.getElementById('CoachId-' + courseId).innerText;
	console.log('Coach Id:', coachId);
	console.log('Course ID:', courseId);
	console.log('Course Name:', courseName);
	console.log('Course Cost:', courseCost);
	console.log('Course Introduce:', courseIntroduce);
	//把抓到的值填入更新表單中
	document.getElementById('utext1').value = courseId;
	document.getElementById('utext2').value = courseName;
	document.getElementById('utext3').value = courseCost;
	document.getElementById('utext4').value = courseIntroduce;
	document.getElementById('chupdate').value = coachId;
}
//更新課程教練選單
const ucoachSelect = document.getElementById('ucoachSelect');
ucoachSelect.addEventListener('change', function() {
	const uselectedCoachId = ucoachSelect.value;
	console.log(uselectedCoachId);
	$('#chupdate').val(uselectedCoachId);

});

