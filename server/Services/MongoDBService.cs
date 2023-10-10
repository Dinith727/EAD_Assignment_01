/*
This is to access the database collections and their CRUD operations. 
Admin, Traveller, Train, Reservation collections can be accessed.

*/

using System.Threading.Tasks;
using web_service.Models;
using web_service.Helper;
using web_service.Operation;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
using MongoDB.Bson;

namespace web_service.Services
{
    public class MongoDBService
    {
        private readonly IMongoCollection<AdminModel> _adminCollection;
        private readonly IMongoCollection<TravellerModel> _travellerCollection;
        private readonly IMongoCollection<TrainScheduleModel> _trainScheduleCollection;
        private readonly IMongoCollection<ReservationModel> _reservationCollection;
        public readonly IMongoCollection<BsonDocument> _genericBsonDoc;

        public MongoDBService(IOptions<MongoDBSettings> mongoDBSettings)
        {
            // Connection with MongoDB
            MongoClient client = new MongoClient(mongoDBSettings.Value.ConnectionURI);
            IMongoDatabase database = client.GetDatabase(mongoDBSettings.Value.DatabaseName);
            //collection references 
            _adminCollection = database.GetCollection<AdminModel>("admin");
            _travellerCollection = database.GetCollection<TravellerModel>("traveller");
            _trainScheduleCollection = database.GetCollection<TrainScheduleModel>("trainShedule");
            _reservationCollection = database.GetCollection<ReservationModel>("reservation");
            _genericBsonDoc = database.GetCollection<BsonDocument>("reservation");

            var usernameIndex = new CreateIndexModel<AdminModel>(
                Builders<AdminModel>.IndexKeys.Ascending(model => model.username),
                new CreateIndexOptions { Unique = true }
            );
            _adminCollection.Indexes.CreateOneAsync(usernameIndex);

        }

        // Admin Services
        public AdminOperations Admin => new AdminOperations(_adminCollection);

        // Traveller Services
        public TravellerOperation Traveller => new TravellerOperation(_travellerCollection);
        public TrainScheduleOperation Train => new TrainScheduleOperation(_trainScheduleCollection);
        public ReservationOperation Reservation => new ReservationOperation(_reservationCollection, _genericBsonDoc);


    }
}