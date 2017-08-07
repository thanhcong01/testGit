var selectedElement = 0;
var currentX = 0;
var currentY = 0;
var currentScaleX = 0;
var currentScaleY = 0;
var currentMatrix = 0;
var lsItem = [];

function selectElement(evt) {
	selectedElement = evt.target;
	currentX = evt.clientX;
	currentY = evt.clientY;
	currentMatrix = selectedElement.getAttributeNS(null, "transform").slice(7,-1).split(' ');

	for(var i=0; i<currentMatrix.length; i++) {
		currentMatrix[i] = parseFloat(currentMatrix[i]);
	}
	selectedElement.setAttributeNS(null, "r", "15");
	selectedElement.setAttributeNS(null, "onmousemove", "moveElement(evt)");
	selectedElement.setAttributeNS(null, "onmouseout", "deselectElement(evt)");
	selectedElement.setAttributeNS(null, "onmouseup", "deselectElement(evt)");
}

function moveElement(evt) {
	var dx = evt.clientX - currentX;
	var dy = evt.clientY - currentY;
//	var dx = parseInt(selectedElement.getAttribute("cx")) + evt.clientX - currentX;
//    var dy = parseInt(selectedElement.getAttribute("cy")) + evt.clientY - currentY;
    
	currentMatrix[4] += dx;
	currentMatrix[5] += dy;
//	selectedElement.setAttribute("cx", dx);
//    selectedElement.setAttribute("cy", dy);
    //console.log(currentX + " - " + currentY + ", "+  evt.clientX + " - " + evt.clientY + ", "+ dx + " - " + dy);
	selectedElement.setAttributeNS(null, "transform", "matrix(" + currentMatrix.join(' ') + ")");
	currentX = evt.clientX;
	currentY = evt.clientY;
}

function deselectElement(evt) {
	if(selectedElement != 0){
		for (var i = 0; i < lsItem.length; i++) {
			var itemAdd = lsItem[i];
			//console.log(itemAdd.positionId + " - " + selectedElement.getAttribute("name"));
			if (itemAdd.positionId == selectedElement.getAttribute("name")) {
				itemAdd.figureX = currentMatrix[4];
				itemAdd.figureY = currentMatrix[5];
				//console.log(itemAdd.figureX + " - " + itemAdd.figureY);
			}
		}
		
		selectedElement.removeAttributeNS(null, "onmousemove");
		selectedElement.removeAttributeNS(null, "onmouseout");
		selectedElement.removeAttributeNS(null, "onmouseup");
		selectedElement.setAttributeNS(null, "r", "10");
		selectedElement = 0;
	}
}

function save_position()
{
	if(!lsItem) return;
	
	savePosition([{"name":"JsonData","value":JSON.stringify(lsItem)}]);
}

function updatesvg() {
	var svgns = "http://www.w3.org/2000/svg";
	
	var features = document.getElementById('features');
	features.innerHTML= '';
	lsItem = JSON.parse($('[id$=hPosData]').val());

	if(!lsItem) return;
	for (var i = 0; i < lsItem.length; i++) {
		var itemAdd = lsItem[i];
		var tagAdd = document.createElementNS(svgns, "circle");
		tagAdd.setAttribute("name", itemAdd.positionId);
		tagAdd.setAttribute("r", "10");
		tagAdd.setAttribute("cx", itemAdd.figureX);
		tagAdd.setAttribute("cy", itemAdd.figureY);
		tagAdd.setAttribute("fill","#009e0f");
		tagAdd.setAttribute("style","cursor: move;");
		tagAdd.setAttribute("transform","matrix(1 0 0 1 0 0)"); 
		tagAdd.setAttribute("onmousedown","selectElement(evt)");
//		tagAdd.setAttribute("onmousemove", "moveElement(evt)");
//		tagAdd.setAttribute("onmouseout", "deselectElement(evt)");
//		tagAdd.setAttribute("onmouseup", "deselectElement(evt)");
		features.appendChild(tagAdd);
	}
	var content = features.innerHTML;
	features.innerHTML = content;
}

