const express = require('express');
const Account = require('../models/account');

const router = express.Router();

/* GET users listing. */
router.get('/:id', (req, res, next) => {
    res.send(`GET user/${req.params.id}`);
});

router.post('/', async (req, res, next) => {
    try {
        const account = new Account(req.body);
        await account.save();

        res.status(201).end();
    } catch (e) {
        res.status(404).json({
            error: 'Failed to create account'
        });
    }
});

module.exports = router;
