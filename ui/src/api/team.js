import request from "@/api";
const baseUrl = request.defaults.baseURL+'/team';


export function getTeamCounts(){
    return request({
        url: baseUrl+'/getTeamCounts',
        method: 'post',
    })
}


export function getFreeTeamId(){
    return request({
        url: baseUrl+'/getFreeTeamId',
        method: 'post',
    })
}


export function addTeam(data){
    return request({
        url: baseUrl+'/addTeam',
        method: 'post',
        data
    })
}

export function getWorkingTeams(){
    return request({
        url: baseUrl+'/getWorkingTeams',
        method: 'post',
    })
}