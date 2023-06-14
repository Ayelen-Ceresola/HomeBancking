let {createApp} = Vue

createApp({
    data (){
        return {
            obj:{
                firstName:"",
                lastName:"",
                email:"",
            },
            clients: [],
            json: "",

        }
    },

    created(){
        this.loadData()

    },

    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients")
            .then((res) => {
                this.json = res.data;
                this.clients = res.data
                console.log(this.clients)
                console.log(this.json)
            })
            .catch(err => console.log (err))
        },

        postClient(){
            axios.post("http://localhost:8080/rest/clients" , this.obj)
            .then((res) => {
                this.loadData()
            }
            )
        },

        addClient(){
            this.postClient()
            


        },
    }
}).mount ("#app")