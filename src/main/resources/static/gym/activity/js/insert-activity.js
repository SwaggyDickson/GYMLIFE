        // 一鍵輸入按鈕點擊事件處理函式
        $('#fill-form-btn').click(function () {
            // 設定表單欄位的值
            $('#activityName').val('2023 蘭陽百K自行車挑戰賽');
            $('#activityDate').val('2023-09-10');
            $('#activityLocation').val('宜蘭縣頭城鎮烏石港重劃區 (環鎮東路二段與青雲路三段390巷1弄交叉口後段道路)');
            $('#organizer').val('聯網國際資訊股份有限公司');
            $('#registrationStartDate').val('2023-05-11');
            $('#registrationEndDate').val('2023-08-10');
            // 由於檔案上傳欄位無法預先輸入，這裡留空
            $('#applicantLimitedQty').val('700');
            $('#activityApplicantsQty').val('480');

            // 在 CKEditor 中插入預定義的內容和圖片
            // 插入已經排版好的 HTML 內容
            var htmlContent = `
                <h1>2023 蘭陽百K自行車挑戰賽</h1>
                <h1>全程100公里</h1>
                <img src="https://www.eventpal.com.tw/97cfa7ad-f457-45cc-99f7-68a1b1d000ee/2023lanyang09101300x980.png" alt="圖片">
                <img src="https://www.eventpal.com.tw/97cfa7ad-f457-45cc-99f7-68a1b1d000ee/1876ee9bbcd.jpg" alt="圖片">
                <p>蘭陽百K自行車賽擁有最豐富的人文風情及地形美景</p>
                <p>三星鄉的長直線平原公路、長埤湖燒腿的陡上坡後隨即迎面而來的陡下坡，還有大同鄉遠眺蘭陽溪風景的台7線路段</p>
                <p>這些都是你不可錯過的理由</p>
                <p>全線經過頭城、壯圍、五結、羅東、三星、大同</p>
                <p>將宜蘭美景盡收眼底，加上各地休閒又具知識性的觀光工廠及到處都有超好拍照的網美景點</p>
                <p>加上此次新增的證書照片護貝服務，當然要找個好地方拍拍美照</p>
            `;
            window.editor.setData(htmlContent);
        });

        // 表單提交事件處理函式
        function submitForm(event) {
            event.preventDefault(); // 阻止表單提交

            // 同步 CKEditor 的內容到對應的文本域
            var editorData = editor.getData();
            $('#activityInfo').val(editorData);

            // 获取表单字段的值
            var activityDate = $('#activityDate').val();
            var registrationStartDate = $('#registrationStartDate').val();
            var registrationEndDate = $('#registrationEndDate').val();

            // 进行日期判断
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
                // 日期判断通过，弹出确认 SweetAlert 提示框
        Swal.fire({
            title: "確認新增",
            text: "您確定要新增嗎？",
            icon: "question",
            showCancelButton: true,
            confirmButtonText: "確定",
            cancelButtonText: "取消"
        }).then(function (result) {
            if (result.isConfirmed) {
                // 使用 AJAX 提交表单
                $.ajax({
                    url: $("#insertForm").attr('action'),
                    type: $("#insertForm").attr('method'),
                    data: new FormData($("#insertForm")[0]),
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        // 提交成功后显示成功的 SweetAlert 提示框
                        Swal.fire({
                            title: "新增成功",
                            icon: "success",
                            showConfirmButton: false,
                            timer: 1500,
                            delay: 2000
                        }).then(function () {
                            // 提示框关闭后进行页面重定向
                            window.location.href = 'http://localhost:8080/gymlife/activity/getAll';
                        });
                    },
                    error: function (error) {
                        // 提交失败后显示失败的 SweetAlert 提示框
                        Swal.fire({
                            title: "新增失败",
                            text: "请稍后再试",
                            icon: "error",
                            confirmButtonText: "确定"
                        });
                    }
                });
            } else if (result.isDismissed) {
                // 显示取消提示
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