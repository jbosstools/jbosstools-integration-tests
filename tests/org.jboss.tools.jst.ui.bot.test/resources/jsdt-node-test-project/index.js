"use strict";
var express = require('express');
var app = express();
var port = process.env.PORT || 3000;

var Book = require('./book.js');
var myFavoriteBook = getMyFavoriteBook();

app.get('/', function(req, res) {
    res.send(myFavoriteBook);
});

app.listen(port, function() {
    console.log('Listening on port ' + port);
});

function getMyFavoriteBook() {
    return new Book('Nineteen Eighty-Four', 'George Orwell');
}