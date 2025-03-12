<script>
    $(document).ready(function () {
    let url = "http://spartacodingclub.shop/sparta_api/seoulair";
    fetch(url).then(res => res.json()).then(data => {
    let mise = data['RealtimeCityAir']['row'][0]['IDEX_NM']
    $('#seoulmise').text(mise)
})
})

    function openclose() {
    $('#postingbox').toggle();
}
    function makeCard() {

    let fileInput = document.getElementById('imageUpload');
    let file = fileInput.files[0];

    if (!file) {
    alert("이미지를 선택하세요!");
    return;
}
    let reader = new FileReader();
    reader.onload = function(e) {
    let imageSrc = e.target.result;
    let title = $('#title').val();
    let content = $('#content').val();

    let temp_html = `
          <div class="col h-100">
            <div class="card">
              <img src="${imageSrc}" class="card-img-top" alt="Uploaded Image">    <!-- 동적으로 이미지 적용 -->

              <div class="card-body">
                <h5 class="card-title">${title}</h5>
                <p class="card-text">${content}</p>
              </div>
            </div>
          </div>`;
    $('#cardgo').append(temp_html);

    fileInput.value = "";
    $('#title').val('');
    $('#content').val('');
};
    reader.readAsDataURL(file);
}
</script>