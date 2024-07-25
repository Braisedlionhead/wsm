import router from '@/router';

const to_router = (rt) => {
    console.log(rt)
    router.push({ name: rt })
}


export default to_router;