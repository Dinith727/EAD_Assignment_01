using BCrypt.Net;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using web_service.Helper;


namespace web_service.Models
{
    public class TrainScheduleModel
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? _id { get; set; }

        public string trainName { get; set; } = null!;

        public string? from { get; set; } = null;

        public string? to { get; set; } = null;

        public DateTime startTime { get; set; }

        public DateTime arrivalTime { get; set; }

        public TrainClass price { get; set; } = null!;

        public bool active { get; set; }

        public bool published { get; set; }

        public bool _isDeleted { get; set; }




    }
}
