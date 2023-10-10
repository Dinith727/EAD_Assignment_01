using System.Threading.Tasks;
using web_service.Models;
using web_service.Helper;
using Microsoft.Extensions.Options;
using MongoDB.Driver;


namespace web_service.Operation
{

    public class TravellerOperation

    {
        private readonly IMongoCollection<TravellerModel> _travellerCollection;

        public TravellerOperation(IMongoCollection<TravellerModel> travellerCollection)
        {
            _travellerCollection = travellerCollection;
        }

        public async Task CreateAsync(TravellerModel travellerModel)
        {
            await _travellerCollection.InsertOneAsync(travellerModel);
        }

        public async Task<TravellerModel> FindByIdAsync(string id)
        {
            var filter = Builders<TravellerModel>.Filter.Eq("_id", id);
            return await _travellerCollection.Find(filter).FirstOrDefaultAsync();
        }
        public async Task<TravellerModel> FindByEmailAsync(string email)
        {
            var filter = Builders<TravellerModel>.Filter.Eq("email", email);
            return await _travellerCollection.Find(filter).FirstOrDefaultAsync();
        }

        public async Task<TravellerModel> UpdateAsync(string id, TravellerModel traveller)
        {



            var updateList = new List<UpdateDefinition<TravellerModel>>();

            updateList.Add(traveller.fullName != null ? Builders<TravellerModel>.Update.Set(rec => rec.fullName, traveller.fullName) : null);
            updateList.Add(traveller.email != null ? Builders<TravellerModel>.Update.Set(rec => rec.email, traveller.email) : null);
            updateList.Add(traveller.NIC != null ? Builders<TravellerModel>.Update.Set(rec => rec.NIC, traveller.NIC) : null);


            updateList.RemoveAll(update => update == null);

            var _update = Builders<TravellerModel>.Update.Combine(updateList);


            var filter = Builders<TravellerModel>.Filter.Eq("_id", id);
            var options = new FindOneAndUpdateOptions<TravellerModel>
            {
                ReturnDocument = ReturnDocument.After,
            };


            var updatedDocument = await _travellerCollection.FindOneAndUpdateAsync(
                filter,
               _update,
                options
            );

            return updatedDocument;
        }
        public async Task<TravellerModel> UpdateActiveAsync(string id, bool _active)
        {

            var _update = Builders<TravellerModel>.Update.Combine(

             Builders<TravellerModel>.Update.Set((rec => rec.active), _active)
            );


            var filter = Builders<TravellerModel>.Filter.Eq("_id", id);
            var options = new FindOneAndUpdateOptions<TravellerModel>
            {
                ReturnDocument = ReturnDocument.After,
            };


            var updatedDocument = await _travellerCollection.FindOneAndUpdateAsync(
                filter,
               _update,
                options
            );

            return updatedDocument;
        }
        public async Task<TravellerModel> UpdatePasswordAsync(string id, string password)
        {

            var _update = Builders<TravellerModel>.Update.Combine(

             Builders<TravellerModel>.Update.Set((rec => rec.password), password)
            );


            var filter = Builders<TravellerModel>.Filter.Eq("_id", id);
            var options = new FindOneAndUpdateOptions<TravellerModel>
            {
                ReturnDocument = ReturnDocument.After,
            };


            var updatedDocument = await _travellerCollection.FindOneAndUpdateAsync(
                filter,
               _update,
                options
            );

            return updatedDocument;
        }

        public async Task<bool> DeleteAsync(string id)
        {
            var filter = Builders<TravellerModel>.Filter.Eq("_id", id);
            var update = Builders<TravellerModel>.Update
                .Set("_isDeleted", true)
                .Set("email", id)
                .Set("active", false);

            var result = await _travellerCollection.UpdateOneAsync(filter, update);
            return result.ModifiedCount > 0;
        }
        public async Task<bool> DeActivateAsync(string id)
        {
            var filter = Builders<TravellerModel>.Filter.Eq("_id", id);
            var update = Builders<TravellerModel>.Update
                .Set("active", false);

            var result = await _travellerCollection.UpdateOneAsync(filter, update);
            return result.ModifiedCount > 0;
        }
        public async Task<List<TravellerModel>> GetActiveAsync()
        {
            var filter = Builders<TravellerModel>.Filter.And(
                Builders<TravellerModel>.Filter.Eq("_isDeleted", false),
                Builders<TravellerModel>.Filter.Eq("active", true)
            );

            var users = await _travellerCollection.Find(filter).ToListAsync();

            return users;
        }
        public async Task<List<TravellerModel>> GetAllAsync()
        {
            var filter = Builders<TravellerModel>.Filter.And(
                Builders<TravellerModel>.Filter.Eq("_isDeleted", false)
            );

            var users = await _travellerCollection.Find(filter).ToListAsync();

            return users;
        }

    }
}