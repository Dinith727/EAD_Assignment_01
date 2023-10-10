Defining CRUD Operations To Be 

using System.Threading.Tasks;
using web_service.Models;
using web_service.Helper;
using Microsoft.Extensions.Options;
using MongoDB.Driver;


namespace web_service.Operation
{

    public class TrainScheduleOperation

    {
        private readonly IMongoCollection<TrainScheduleModel> _trainScheduleCollection;

        public TrainScheduleOperation(IMongoCollection<TrainScheduleModel> travellerCollection)
        {
            _trainScheduleCollection = travellerCollection;
        }

        public async Task CreateAsync(TrainScheduleModel trainScheduleModel)
        {
            await _trainScheduleCollection.InsertOneAsync(trainScheduleModel);
        }

        public async Task<TrainScheduleModel> FindByIdAsync(string id)
        {
            var filter = Builders<TrainScheduleModel>.Filter.Eq("_id", id);
            return await _trainScheduleCollection.Find(filter).FirstOrDefaultAsync();
        }

        public async Task<TrainScheduleModel> UpdateAsync(string id, TrainScheduleBase trainSchedule)
        {


            var updateList = new List<UpdateDefinition<TrainScheduleModel>>();

            updateList.Add(trainSchedule.trainName != null ? Builders<TrainScheduleModel>.Update.Set(rec => rec.trainName, trainSchedule.trainName) : null);
            updateList.Add(trainSchedule.from != null ? Builders<TrainScheduleModel>.Update.Set(rec => rec.from, trainSchedule.from) : null);
            updateList.Add(trainSchedule.to != null ? Builders<TrainScheduleModel>.Update.Set(rec => rec.to, trainSchedule.to) : null);
            updateList.Add(trainSchedule.startTime != null ? Builders<TrainScheduleModel>.Update.Set(rec => rec.startTime, trainSchedule.startTime) : null);
            updateList.Add(trainSchedule.arrivalTime != null ? Builders<TrainScheduleModel>.Update.Set(rec => rec.arrivalTime, trainSchedule.arrivalTime) : null);
            updateList.Add(trainSchedule.price != null ? Builders<TrainScheduleModel>.Update.Set(rec => rec.price, trainSchedule.price) : null);
            updateList.Add(trainSchedule.active != null ? Builders<TrainScheduleModel>.Update.Set(rec => rec.active, trainSchedule.active) : null);
            updateList.Add(trainSchedule.published != null ? Builders<TrainScheduleModel>.Update.Set(rec => rec.published, trainSchedule.published) : null);


            updateList.RemoveAll(update => update == null);

            var _update = Builders<TrainScheduleModel>.Update.Combine(updateList);


            var filter = Builders<TrainScheduleModel>.Filter.Eq("_id", id);
            var options = new FindOneAndUpdateOptions<TrainScheduleModel>
            {
                ReturnDocument = ReturnDocument.After,
            };


            var updatedDocument = await _trainScheduleCollection.FindOneAndUpdateAsync(
                filter,
               _update,
                options
            );

            return updatedDocument;
        }


        public async Task<bool> DeleteAsync(string id)
        {
            var filter = Builders<TrainScheduleModel>.Filter.Eq("_id", id);
            var update = Builders<TrainScheduleModel>.Update
                .Set("_isDeleted", true)
                .Set("active", false)
                .Set("published", false);

            var result = await _trainScheduleCollection.UpdateOneAsync(filter, update);
            return result.ModifiedCount > 0;
        }
        public async Task<List<TrainScheduleModel>> GetActiveAsync()
        {
            var filter = Builders<TrainScheduleModel>.Filter.And(
                Builders<TrainScheduleModel>.Filter.Eq("_isDeleted", false),
                Builders<TrainScheduleModel>.Filter.Eq("active", true),
                Builders<TrainScheduleModel>.Filter.Eq("published", true)
            );

            var users = await _trainScheduleCollection.Find(filter).ToListAsync();

            return users;
        }
        public async Task<List<TrainScheduleModel>> GetAllAsync()
        {
            var filter = Builders<TrainScheduleModel>.Filter.And(
                Builders<TrainScheduleModel>.Filter.Eq("_isDeleted", false)
            );

            var users = await _trainScheduleCollection.Find(filter).ToListAsync();

            return users;
        }

    }
}