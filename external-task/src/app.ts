import express from 'express';

const app = express();
const port = 3000;

app.get('/home', (req,res)=>{
    res.status(200).json({'Message':'Welcome to external task service'});
});

app.listen(port, () => {

    return console.log(`Server listening on ${port}`);
});