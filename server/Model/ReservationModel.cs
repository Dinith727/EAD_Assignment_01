//model for reservations 
using BCrypt.Net;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;


namespace web_service.Models
{
    public class ReservationModel
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)] 
        public string? _id { get; set; }

        [BsonRepresentation(BsonType.ObjectId)]
        public string? userID { get; set; } // Identifier for the user associated with the reservation.

        public string? trainClass { get; set; }

        public DateTime date { get; set; }

        [BsonRepresentation(BsonType.ObjectId)] 
        public string train { get; set; } = null!; // Identifier for the train associated with the reservation.
        //non nullable property


        [BsonRepresentation(BsonType.ObjectId)]
        public string travelAgent { get; set; } = null!;  //non nullable property

        public string agentNote { get; set; } = null!;

        public string? status { get; set; } = null;

        public bool _isDeleted { get; set; }


    }
}
