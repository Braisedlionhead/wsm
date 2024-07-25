import request from "@/api";
const baseUrl = request.defaults.baseURL+'/order';


export function listOrders(){
    return request({
        url: baseUrl+'/list',
        method: 'post',
    })
}



export function showStatus(){
    return request({
        url: baseUrl+'/showStatus',
        method: 'post',
    })
}


export function listPageOrder(data){
    return request({
        url: baseUrl+'/listPage',
        method: 'post',
        data: data,
    })
}


export function listPageByStatusAndTeam(data){
    return request({
        url: baseUrl+'/listPageByStatusAndTeam',
        method: 'post',
        data: data,
    })
}

export function addOrder(data){
    return request({
        url: baseUrl+'/add',
        method: 'post',
        data: data,
    })
}


export function modifyOrder(data){
    return request({
        url: baseUrl+'/modify',
        method: 'post',
        data: data,
    })
}


export function deleteOrder(data){
    return request({
        url: baseUrl+'/delete',
        method: 'post',
        data: data,
    })
}

export function findByPartId(data){
    return request({
        url: baseUrl+'/findByPartId',
        method: 'post',
        data: data,
    })
}
