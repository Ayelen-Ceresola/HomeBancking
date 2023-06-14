let {createApp} = Vue

createApp({
    data(){
        return {
            clients:[],
            account1:{},
         
        }
    },

    created(){
        this.loadData()

    },


    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients/1")
            .then((res) => {
                this.clients = res.data
                console.log(this.clients)
                this.account1= res.data.accounts
                this.account1.sort((a,b)=> a.id - b.id)


            })
            .catch(err => console.log (err))
        },
    }

}).mount ("#app")