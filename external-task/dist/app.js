"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const app = express_1.default();
const port = 3000;
app.get('/home', (req, res) => {
    res.status(200).json({ 'Message': 'Welcome to external task service' });
});
app.listen(port, () => {
    return console.log(`Server listening on ${port}`);
});
//# sourceMappingURL=app.js.map