// This controller handels HTTP Requests For Train management Function
using Microsoft.AspNetCore.Mvc;
using System;
using web_service.Services;
using web_service.Models;
using web_service.Utility;
using web_service.Helper;
using web_service.Formatter;
using Microsoft.AspNetCore.Authorization;


namespace web_service.Controllers;

[ApiController]
[Route("api/[controller]")]
public class TrainScheduleController : ControllerBase
{

    private readonly MongoDBService _mongoDBService;

    public TrainScheduleController(MongoDBService mongoDBService)
    {
        _mongoDBService = mongoDBService;
    }


    // 'GET' request to fetch a train by its ID
    
    //authorization  levels for all the users
    [Authorize(Roles = "backOffice,travelAgent,traveller")]
    [HttpGet]
    async public Task<IActionResult> Get([FromQuery] string? id)
    {
        try
        {
            var val = await _mongoDBService.Train.FindByIdAsync(id ?? "");

            if (val == null)
            {
                return NotFound(new ErrorResponse
                {
                    ErrorCode = 404,
                    Message = "Not Found",
                    Details = "Requested train not Found"
                });
            }

            return Ok(new DataResponse<TrainScheduleModel>
            {
                status = 200,
                data = val
            });
        }
        catch (System.Exception ex)
        {

            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }

    }

    // defining authorized roles to perform this function.
    [Authorize(Roles = "backOffice")]
    // 'GET' request to fetch all trains list
    [HttpGet("all-trains")]
    async public Task<IActionResult> GetAll([FromQuery] bool active)
    {
        try
        {
            List<TrainScheduleModel> val = new List<TrainScheduleModel>();
            if (active)
            {
                val = await _mongoDBService.Train.GetActiveAsync();

            }
            else
            {

                val = await _mongoDBService.Train.GetAllAsync();
            }



            return Ok(new DataResponse<List<TrainScheduleModel>>
            {
                status = 200,
                data = val
            });
        }
        // error handling 
        catch (System.Exception ex)
        {

            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }

    }

    [Authorize(Roles = "backOffice,travelAgent,traveller")]
    // Http 'GET' request to fetch all active trains
    [HttpGet("active-trains")]
    async public Task<IActionResult> GetAllUser()
    {
        try
        {
            var val = await _mongoDBService.Train.GetActiveAsync();
            return Ok(new DataResponse<List<TrainScheduleModel>>
            {
                status = 200,
                data = val
            });
        }
        catch (System.Exception ex)
        {

            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }

    }

    // only backoffice users can perform this function
    [Authorize(Roles = "backOffice")]

    // HTTP 'POST' request to add new trains
    [HttpPost("add")]
    async public Task<IActionResult> Add([FromBody] TrainScheduleModel train)
    {

        try
        {

            train.active = true;
            train.published = false;
            train._isDeleted = false;
            await _mongoDBService.Train.CreateAsync(train);

            return Ok(new DataResponse<TrainScheduleModel>
            {
                status = 200,
                data = train
            });
        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }
    }

    // only backoffice users can perform this function
    [Authorize(Roles = "backOffice")]
    // Update Request
    [HttpPost("update")]
    async public Task<IActionResult> Update([FromBody] TrainScheduleBase _train)
    {
        try
        {


            var _trainShedule = await _mongoDBService.Reservation.FindByTrainIdAsync(_train._id ?? "");

            if (_trainShedule.Count > 0)
            {
                return BadRequest(new ErrorResponse
                {
                    ErrorCode = 400,
                    Message = "Cannot Perform Delete",
                    Details = "The Train was booked by " + _trainShedule.Count.ToString() + " reservation/s."
                });
            }

            var _res = await _mongoDBService.Train.UpdateAsync(_train._id ?? "", _train);

            if (_res == null)
            {

                return BadRequest(new ErrorResponse
                {
                    ErrorCode = 400,
                    Message = "Update Unsuccessfull",
                    Details = "The Train detail update unsucessful"
                });
            }
            else
            {
                return Ok(new DataResponse<TrainScheduleModel>
                {
                    status = 200,
                    data = _res
                });

            }


        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }

    }

    // only backoffice users can perform this function
    [Authorize(Roles = "backOffice")]
    // HTTP Delete function
    [HttpDelete("delete")]
    // public IActionResult Update([FromBody] AdminModel model)
    async public Task<IActionResult> Delete([FromQuery] string id)
    {
        try
        {

            var _trainShedule = await _mongoDBService.Reservation.FindByTrainIdAsync(id);

            if (_trainShedule.Count > 0)
            {
                return BadRequest(new ErrorResponse
                {
                    ErrorCode = 400,
                    Message = "Cannot Perform Delete",
                    Details = "The Train was booked by " + _trainShedule.Count.ToString() + " reservation/s."
                });
            }

            await _mongoDBService.Train.DeleteAsync(id);
            return Ok(new DataResponse<string>
            {
                status = 200,
                data = "Succesfully Deleted"
            });
        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }
    }

}
