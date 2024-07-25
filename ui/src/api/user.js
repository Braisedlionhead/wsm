import request from "@/api";
const baseUrl = request.defaults.baseURL+'/user';

export function list(){
    return request({
        url: baseUrl+'/list',
        method: 'post',
    })
}

export function like(data){
    return request({
        url: baseUrl+'/like',
        method: 'post',
        data: data
    })
}


export function findById(data){
    return request({
        url: baseUrl+'/findById',
        method: 'post',
        data: data
    })
}

export function listPage(data){
    return request({
        url: baseUrl+'/listPageUser',
        method: 'post',
        data: data
    })
}

export function modify(data){
    return request({
        url: baseUrl+'/mod',
        method: 'post',
        data: data
    })
}

export function login(data){
    return request({
        url: '/login',
        method: 'post',
        data: data
    })
}


export function logout() {
    return request({
        url: '/logout',
        method: 'post',
    })
}


export function addUser(data){
    return request({
        url: baseUrl+'/add',
        method: 'post',
        data: data
    })
}


export function searchByNicknameLike(data){
    return request({
        url: baseUrl+'/searchByNicknameLike',
        method: 'post',
        data: data
    })
}


export function getNoTeamUser(data){
    return request({
        url: baseUrl+'/getNoTeamUser',
        method: 'post',
        data: data
    })
}


export function deleteUser(data) {
    return request({
        url: baseUrl+'/delete',
        method: 'post',
        data: data
    })
}