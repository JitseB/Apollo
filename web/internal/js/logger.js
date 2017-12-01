function log(text, color) {
  console.log(getTime() + ' %c' + text, 'color:' + color);
}

function getTime() {
  var now = new Date();
  return (
    (now.getHours() < 10 ? ("0" + now.getHours()) : now.getHours()) + ':' 
    + (now.getMinutes() < 10 ? ("0" + now.getMinutes()) : now.getMinutes()) + ':' 
    + (now.getSeconds() < 10 ? ("0" + now.getSeconds()) : now.getSeconds())
  );
}