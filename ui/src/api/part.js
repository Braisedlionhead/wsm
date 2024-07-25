import request from "@/api";
const baseUrl = request.defaults.baseURL+'/part';



export function list(){
    return request({
        url: baseUrl+'/list',
        method: 'post',
    })
}

export function listPagePart(data){
    return request({
        url: baseUrl+'/listPagePart',
        method: 'post',
        data: data
    })
}


export function likePagePart(data){
    return request({
        url: baseUrl+'/likePagePart',
        method: 'post',
        data: data
    })
}

export function delivery(data){
    return request({
        url: baseUrl+'/delivery',
        method: 'post',
        data: data
    })
}

