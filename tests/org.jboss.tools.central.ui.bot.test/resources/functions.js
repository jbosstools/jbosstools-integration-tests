function searchFor(query){
	$( "#search" ).val(query);
	var e = jQuery.Event("keyup");
	e.which=13;
	e.keyCode=13;
	$ ( "#search" ).trigger(e);
}

function getHTML(){
	return document.documentElement.innerHTML;
}

function getExamples(){
	if ($( "#results" ).hasClass("hidden")){
		return null;
	}
	var resultsDiv = getCurrentResultsDiv();
	if (resultsDiv.hasClass("hidden")){
		return false;
	}
	return getEachInnerText(resultsDiv.find("a"));
}

function getDescriptionForExample(exampleName){
	if ($( "#results" ).hasClass("hidden")){
		return null;
	}
 	var resultsDivs = getCurrentResultsDiv();
 	var resultDiv = getResultDivForExample(resultsDivs, exampleName);
	return getEachInnerText(resultDiv.find(".list-group-item-text"));
}

function getLabelsForExample(exampleName){
	if ($( "#results" ).hasClass("hidden")){
		return null;
	}
	var resultsDivs = getCurrentResultsDiv();
	var resultDiv = getResultDivForExample(resultsDivs, exampleName);
	return getEachInnerText(resultDiv.find("li"));
}

function nextPage(){
	if ($( "#results" ).hasClass("hidden")){
		return null;
	}
	return $( "nav.pull-right" ).find(".next").click();
}

function hasNext(){
	if ($( "#results" ).hasClass("hidden")){
		return null;
	}
	return !$( "nav.pull-right" ).find(".next").hasClass("hidden");
}

function hasPrevious(){
	if ($( "#results" ).hasClass("hidden")){
		return null;
	}
	return !$( "nav.pull-right" ).find(".prev").hasClass("hidden");
}

function prevPage(){
	if ($( "#results" ).hasClass("hidden")){
		return null;
	}
	return $( "nav.pull-right" ).find(".prev").click();
}

function getWizards(){
	if ($( "#home" ).hasClass("hidden")){
		return null;
	}
	return getEachInnerText($( "#wizards" ).find('a'));
}

function clickWizard(name){
	$( "#wizards" ).find('a').filter(":contains('"+name+"')").click();
}

function clickExample(name){
	if ($( "#results" ).hasClass("hidden")){
		return null;
	}
	var resultsDivs = getCurrentResultsDiv();
 	getResultDivForExample(resultsDivs, name).find("a").click();
}

function clearSearch(){
	$("a").filter(":contains('clear search')").click();
}

//Internal functions from now on. Do not call them from Java tests.

function getCurrentResultsDiv(){
	return $( "#resultList" ).find('div');
}

function getResultDivForExample(resultsDivs, exampleName){
	return resultsDivs.filter(":contains('"+exampleName+"')");
}

function getEachInnerText(topElement){
	var resultString="";
	var prefix = "";
	topElement.each(function(index, element){
		resultString += prefix+element.textContent;
		prefix=";";
	});
	return resultString;
}