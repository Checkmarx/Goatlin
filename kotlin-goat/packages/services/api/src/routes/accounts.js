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

        res.status(201).json({});
    } catch (e) {
        let status = 404;
        let error = 'Failed to create account';

        if (e.name === 'MongoError' && e.code === 11000) {
            status = 409;
            error = 'Email address is already registered'
        }

        res.status(status).json({ error });
    } finally {
        res.end();
    }
});

module.exports = router;
