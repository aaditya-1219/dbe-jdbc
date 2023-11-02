const express = require('express');
const app = express();
const bodyParser = require('body-parser')

const { MongoClient } = require('mongodb');

// Connection URL for the local MongoDB server
const url = 'mongodb://localhost:27017'; // Default MongoDB port is 27017

// Database Name
const dbName = 'crud'; // Replace with your database name

let client; // Declare the client outside of the connectToMongoDB function
app.use(bodyParser.json())

async function connectToMongoDB() {
    try {
        // Use the MongoClient to connect to the database
        client = new MongoClient(url, { useNewUrlParser: true, useUnifiedTopology: true });
        await client.connect();

        console.log('Connected to the database');
    } catch (err) {
        console.error('Error connecting to MongoDB:', err);
    }
}


// Call the function to connect to MongoDB when the server starts
connectToMongoDB();

// Middleware to ensure the database connection is available for each request
app.use((req, res, next) => {
    req.db = client.db(dbName);
    next();
});

app.use(express.static('public'))

// Define a route to add data to the database
app.post("/formPost", async (req, res) => {
    try {
        const { db } = req; // Access the database from the request object
        const { name, userId } = req.body;

        // Access the 'user' collection and insert the new data
        const collection = db.collection('user'); // Replace with the name of your collection
        const result = await collection.insertOne({ name, userId })
        .then((response) => res.json(response))

        if (result.acknowledged === 1) {
            res.status(201).json({ message: 'Data added successfully' });
        } else {
            res.status(500).json({ error: 'Failed to add data' });
        }
    } catch (err) {
        console.error('Error adding data to MongoDB:', err);
        res.status(500).json({ error: err });
    }
});


// Define a route to retrieve data from the database
app.get("/", async (req, res) => {
    try {
        const { db } = req; // Access the database from the request object
        // Access a specific collection and retrieve data
        const collection = db.collection('user'); // Replace with the name of your collection
        const data = await collection.find({}).toArray();
        const formattedData = data.map(element => ({
            name: element.name,
            userId: element.userId
        }));

        res.json(formattedData);
        // Send the retrieved data as a JSON response
        // res.json(data);
    } catch (err) {
        console.error('Error retrieving data from MongoDB:', err);
        res.status(500).json({ error: 'Failed to retrieve data' });
    }
});

app.listen(3000, () => {
    console.log("Server running...");
})