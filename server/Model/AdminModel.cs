//Admin model
using BCrypt.Net;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;


namespace web_service.Models
{
    public class AdminModel
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? _id { get; set; }
        public string username { get; set; } = null!;

        public string password { get; set; } = null!;

        public string? role
        {
            get; set;
        } = null;

        public bool active { get; set; }

        public bool _isDeleted { get; set; }

        public void HashPassword()
        {
            // Hash the password and store it back in the Password property
            password = BCrypt.Net.BCrypt.HashPassword(password);

        }

        public bool VerifyPassword(string candidatePassword)
        {
            // Verify a candidate password against the hashed password
            return BCrypt.Net.BCrypt.Verify(candidatePassword, password);
        }
    }
}
