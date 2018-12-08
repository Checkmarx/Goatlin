const mongoose = require('mongoose');

const accountSchema = new mongoose.Schema({
    email: String,
    password: String
});

module.exports = new mongoose.model('Account', accountSchema);
