$('#buybtn').on('click', function(e) {
	e.preventDefault();
	$('#buycourseId').click();
})
//課程價格計算
function calculateCost() {
  let courseCost = $('#courseCost').val();
  let buynumber = $('#buynumber').val();

  $('#allcost').text(buynumber * courseCost);
  $('#corderCost').val(buynumber * courseCost);
	console.log($('#corderCost').val())
}
$('#buynumber').on('change', function(e) {
	calculateCost();
});
//購買堂數按鈕
$('#input-plus').on('click', function() {
  let buynumber = $('#buynumber');
  buynumber.val(parseInt(buynumber.val()) + 1);
  calculateCost();
});
$('#input-minus').on('click', function() {
	let buynumber = $('#buynumber');
  if (parseInt(buynumber.val()) > 1) {
    buynumber.val(parseInt(buynumber.val())-1);
    calculateCost();
  }
});
