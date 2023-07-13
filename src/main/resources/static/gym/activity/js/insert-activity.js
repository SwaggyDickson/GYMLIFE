// 一鍵輸入按鈕點擊事件處理函式
$('#fill-form-btn').click(function() {
	// 設定表單欄位的值
	$('#activityName').val('暢秋萬聖路跑嘉年華-微笑路跑i運動');
	$('#activityDate').val('2023-10-28');
	$('#activityLocation').val('臺北市立兒童新樂園入口處旁');
	$('#organizer').val('中華身障體育運動協會、社團法人中華身心障礙者職業技藝協會');
	$('#registrationStartDate').val('2023-06-17');
	$('#registrationEndDate').val('2023-09-03');
	// 由於檔案上傳欄位無法預先輸入，這裡留空
	$('#applicantLimitedQty').val('800');
	$('#activityApplicantsQty').val('650');

	// 在 CKEditor 中插入預定義的內容和圖片
	// 插入已經排版好的 HTML 內容
	var htmlContent = `<h2><span style="color:hsl(0,0%,100%);">2023年萬聖節不容錯過的盛宴！</span></h2><h2><span style="color:hsl(0,0%,100%);">親子 x 樂園 x 路跑 x 變裝</span></h2><p>&nbsp;</p><p><span style="color:hsl(0,0%,100%);"><img src="https://imagedelivery.net/aJ0ay8hJuGoaVas3rlfLyg/activity/5726-7187245bb6e73602ebd6fd60f5a5ae4b/activity_content/images/20230707-181011-c862492d7158c9025f5bf3515ae32c1e.jpg/public" alt="圖片">&nbsp;</span></p><p><span style="color:hsl(0,0%,100%);"><img src="https://imagedelivery.net/aJ0ay8hJuGoaVas3rlfLyg/activity/5726-7187245bb6e73602ebd6fd60f5a5ae4b/activity_content/images/20230707-181009-5a9f4ce52bd24f3b5e733df5557298ac.jpg/public" alt="圖片"></span></p><p><span style="color:hsl(0,0%,100%);">厭倦了路跑總是要早起嗎？覺得路跑好像太過困難？路跑後沒有地方放鬆休息嗎？暢秋萬聖路跑嘉年華讓你三大訴求一次滿足！</span></p><p><span style="color:hsl(0, 0%, 100%);">來暢秋萬聖路跑嘉年華你可以：</span></p><p><span style="color:hsl(0,0%,100%);">變裝路跑，展現最不一樣的自己</span></p><p><span style="color:hsl(0,0%,100%);">體驗全新路跑路線，在雙溪河濱公園優美的河景中漫步</span></p><p><span style="color:hsl(0,0%,100%);">不用再起早貪黑，在秋高氣爽的午後愜意開跑</span></p><p><span style="color:hsl(0,0%,100%);">特設創意市集、街頭演出，讓路跑後不再無聊</span></p><p><span style="color:hsl(0,0%,100%);">兒童新樂園首次合作路跑！路跑後暢快遊玩新樂園設施，瘋玩一整天</span></p>`;
	window.editor.setData(htmlContent);
});

// 表單提交事件處理函式
function submitForm(event) {
	event.preventDefault(); // 阻止表單提交

	// 獲取所有必填欄位
	var requiredFields = document.querySelectorAll('[required]');
	var emptyFields = []; // 紀錄空字段的名稱

	// 映射欄位 ID 和對應的名稱
	var fieldNames = {
		activityName: '活動名稱',
		activityDate: '活動日期',
		activityLocation: '活動地點',
		organizer: '主辦單位',
		registrationStartDate: '報名開始日期',
		registrationEndDate: '報名結束日期',
		applicantLimitedQty: '報名人數上限',
		activityApplicantsQty: '目前報名人數',
		activityCategory: '活動類別',
		activityStatus: '報名狀態',
		activityCover: '活動封面'
	};

	// 檢查每個必填欄位是否為空
	requiredFields.forEach(function(field) {
		if (field.value.trim() === '') {
			// 紀錄空欄位的名稱
			var fieldName = fieldNames[field.getAttribute('name')];
			if (fieldName) {
				emptyFields.push(fieldName);
			}
			// 添加錯誤樣式
			field.classList.add('is-invalid');
		} else {
			// 移除錯誤樣式
			field.classList.remove('is-invalid');
		}
	});

	// 如果存在空欄位，阻止表單提交並顯示錯誤訊息
	if (emptyFields.length > 0) {
		var errorMessage = '請輸入必填欄位：';
		errorMessage += emptyFields.join('、');
		Swal.fire("錯誤", errorMessage, "error");
		return;
	}

	// 同步 CKEditor 的內容到對應的文本域
	var editorData = editor.getData();
	$('#activityInfo').val(editorData);

	// 獲取表單字段的值
	var activityDate = $('#activityDate').val();
	var registrationStartDate = $('#registrationStartDate').val();
	var registrationEndDate = $('#registrationEndDate').val();

	// 進行日期判斷
	var currentDate = new Date();
	if (currentDate < new Date(registrationStartDate)) {
		// 報名尚未開始，弹出 SweetAlert 提示框
		Swal.fire("日期錯誤", "報名時間尚未開始，請重新選擇。", "warning");
	} else if (currentDate > new Date(registrationEndDate)) {
		// 報名已結束，弹出 SweetAlert 提示框
		Swal.fire("日期錯誤", "報名時間已結束，請重新選擇。", "warning");
	} else if (currentDate > new Date(activityDate)) {
		// 活動已結束，弹出 SweetAlert 提示框
		Swal.fire("日期錯誤", "活動已結束，請重新選擇。", "warning");
	} else {
		// 日期判斷通過，彈出確認 SweetAlert 提示框
		Swal.fire({
			title: "確認新增",
			text: "您確定要新增嗎？",
			icon: "question",
			showCancelButton: true,
			confirmButtonText: "確定",
			cancelButtonText: "取消"
		}).then(function(result) {
			if (result.isConfirmed) {
				// 使用 AJAX 提交表單
				$.ajax({
					url: $("#insertForm").attr('action'),
					type: $("#insertForm").attr('method'),
					data: new FormData($("#insertForm")[0]),
					processData: false,
					contentType: false,
					success: function(response) {
						// 提交成功后显示成功的 SweetAlert 提示框
						Swal.fire({
							title: "新增成功",
							icon: "success",
							showConfirmButton: false,
							timer: 1500,
							delay: 2000
						}).then(function() {
							// 提示框關閉後重新跳轉頁面
							window.location.href = 'http://localhost:8080/gymlife/activity/getAll';
						});
					},
					error: function(error) {
						// 提交失敗後顯示的 SweetAlert 
						Swal.fire({
							title: "新增失敗",
							text: "請稍後再試",
							icon: "error",
							confirmButtonText: "確定"
						});
					}
				});
			} else if (result.isDismissed) {
				// 顯示取消提示
				Swal.fire("取消", "", "info");
			}
		});
	}
}

// 根據日期來做判斷
// 獲取URL中的alert參數
const urlParams = new URLSearchParams(window.location.search);
const alertMessage = urlParams.get('alert');

// 如果存在alert参数，使用SweetAlert顯示提示框
if (alertMessage) {
	Swal.fire({
		title: '日期錯誤',
		text: alertMessage,
		icon: 'warning',
		confirmButtonText: '確定'
	});
}