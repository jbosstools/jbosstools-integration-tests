	var data = [{
	  name: 'Cheese',
	  id: '1'
	}, {
	  name: 'Doughnut',
	  id: '2'
	}, {
	  name: 'Cookies',
	  id: '3'
	}];

var products = {

  getAll: function(req, res) {
    var allProducts = [{
	  name: 'Ice Cream',
	  id: '1'
	}, {
	  name: 'Doughnut',
	  id: '2'
	}, {
	  name: 'Cookies',
	  id: '3'
	}];// Spoof a DB call
    res.json(allProducts);
  },

  getOne: function(req, res) {
    var id = req.params.id;
    var product = data[0]; // Spoof a DB call
    res.json(product);
  },

  create: function(req, res) {
    var newProduct = req.body;
    data.push(newProduct); // Spoof a DB call
    res.json(newProduct);
  },

  update: function(req, res) {
    var updateProduct = req.body;
    var id = req.params.id;
    data[id] = updateProduct // Spoof a DB call
    res.json(updateProduct);
  },

  delete: function(req, res) {
    var id = req.params.id;
    data.splice(id, 1) // Spoof a DB call
    res.json(true);
  }
};



module.exports = products;
