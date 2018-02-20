console.log('before', $("input:radio[id=option1]").prop('checked'));

// $("input:radio[id=option1]").prop('checked', true);

$("input:radio[id=option2]").click();
console.log('after', $("input:radio[id=option1]").prop('checked'));