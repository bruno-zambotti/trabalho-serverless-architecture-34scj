# Trabalho da disciplina de Serverless Architecture 34SCJ.
**Aluno:** Bruno Delphino Zambotti (RM 334242)

**Professor:** Peterson de Oliveira Larentis.

**Descrição:** Projeto do trabalho final da disciplina de Serverless Architecture da turma 34SCJ da FIAP.

## Tecnologias e ferramentas utilizadas 
- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Para compilar e executar os códigos descritos nesta aplicação.
- [Maven](https://maven.apache.org/install.html) - Para gerenciamento das dependências da aplicação.
- [Python 3](https://www.python.org/downloads/) - Para permitir a execução dos comando do AWS CLI.
- [AWS CLI](https://docs.aws.amazon.com/pt_br/cli/latest/userguide/install-cliv1.html) - Pra disponibilizar os comandos necessários para criação dos serviços utilizados.
- [AWS SAM (Serverless Application Model)](https://github.com/awslabs/aws-sam-cli) - Pra prover a infraestrutura da aplicação como código.
- [Docker](https://www.docker.com/community-edition) - Para execução da aplicação localmente.
- [Amazon API Gateway](https://aws.amazon.com/api-gateway) - Para gerenciar as requisições a aplicação.
- [AWS Lambda](https://aws.amazon.com/lambda) - Para executar o código serverless desenvolvido.
- [Amazon DynamoDB](https://aws.amazon.com/dynamodb) - Banco não relacional, para armazenar e consultar as informações persistidas.

## Descritivo do Trabalho
Desenvolver uma API seguindo as melhores práticas do conjunto de princípios da arquitetura REST (Representacional State Transfer).

### Requisitos funcionais:
- RF1 - Criar uma entidade chamada Trip contendo os atributos: 
> - id
> - date (YYYY/MM/DD) 
> - country 
> - city 
> - URL (Referente ao repositório para fotos).

- RF2 - Permitir a criação de um novo registro de viagem:
> - Retornar no header da resposta o HTTP Status Code 201 - Created.
> - Retornar no body da resposta o id da Trip e uma url de um bucket (Amazon S3) que será utilizado para posteriormente subir as fotos da viagem. O nome do bucket deve ser gerado com a seguinte sintaxe: ```<trip-country>-<trip-city>-<date>-<6-digit-random-number>```

- RF3 - Permitir a busca de dados das viagens realizadas por período:
> - Na requisição o período deverá ser recebido via query parameter: ```/trips?start=YYYY-MM-DD&end=YYYY-MM-DD```
> - Retornar no header da resposta o HTTP Status Code 200 - OK.
> - Retornar no body da resposta uma lista contendo os resultados (todos os campos da entidade).
> - Em caso de não retornar resultados para os parâmetros informados, retornar uma lista vazia, mantendo o HTTP Status Code 200 - OK.

- RF4 - Permitir a busca de dados de uma viagem por id.
> - Na requisição o período deverá ser recebido via path parameter: ```/trips/{id}```
> - Retornar somente os dados da viagem referente ao id informado.
> - Caso os registros da viagem sejam encontrados, deverá ser retornado o HTTP Status Code 200 - OK e no body da resposta os dados da viagem.
> - Caso os registros da viagem não sejam encontrados, deverá ser retornado o HTTP Status Code 404 - Not Found, e o body da resposta como vazio.

### Requisitos não funcionais:
- RNF1 - O trabalho deverá conter um arquivo README com as instruções para rodar a aplicação localmente e também na AWS.

- RNF2 - A aplicação deve ser capaz de ser executada com a Stack completa localmente.

- RNF3 - Deverá ser possível implantar a aplicação na AWS através dos comandos do AWS SAM.

## Funcionalidades:

- Armazenar os dados de uma viagem
```(POST -> /trips)```

- Consultar as informações das viagens por um período
```(GET -> /trips?start=YYYY-MM-DD&end=YYYY-MM-DD)```

- Consultar as informações de uma viagem
```(GET -> /trips/{id})```

## Execução local em computador pessoal

### Configuração do ambiente
Deverá ser realizada conforme o sistema operacional utilizado, para mais informações sobre cada ambiente, acesse os links disponíveis em [tecnologias e ferramentas utilizadas](#Tecnologias-e-ferramentas-utilizadas) no início deste documento.                                                                                                                  

### Subindo o banco de dados e a aplicação
1. Iniciar o DynamoDB localmente com Docker. 
- Linux ou MacOs: `docker run -p 8000:8000 -v $(pwd)/local/dynamodb:/data/ amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath /data`
- Windows (Powershell): `docker run -p 8000:8000 -v $pwd\local\dynamodb:\data\ amazon\dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath \data`

2. Realizar a criação da tabela no DynamoDB. `aws dynamodb create-table --table-name trips --attribute-definitions AttributeName=id,AttributeType=S AttributeName=dateTimeCreation,AttributeType=S --key-schema AttributeName=id,KeyType=HASH AttributeName=dateTimeCreation,KeyType=RANGE --billing-mode PAY_PER_REQUEST --endpoint-url http://localhost:8000`
    
    2.1. Caso a tabela já exista, é possível exclui-la com o seguinte comando: `aws dynamodb delete-table --table-name trips --endpoint-url http://localhost:8000`

3. Para iniciar o AWS SAM API localmente, informe o comando, conforme o seu sistema operacional:
 - MacOS: `sam local start-api --env-vars src/test/resources/test_environment_mac.json`
 - Windows: `sam local start-api --env-vars src\test\resources\test_environment_windows.json`
 - Linux: `sam local start-api --env-vars src/test/resources/test_environment_linux.json`

4. Para realizar requisições a função localmente através do API Gateway utilize a collection, para mais informações sobre como utilizar o Postman acesse [testando a aplicação via Postman](#Testando-a-aplicação-via-Postman).

## Execução local utilizando o Cloud9

Abaixo é representado o passo a passo para configuração do ambiente utilizando o Cloud9:

### Configuração do ambiente

Se é a primeira vez que você utiliza o Cloud9, favor seguir os passos descritos neste [anexo](../master/attachments/setup-cloud9/README.md).

Algumas observações:
- Java 8 já vem instalado
- Docker já vem instalado

#### Instalando o pip3
`curl -O https://bootstrap.pypa.io/get-pip.py`

`python3 get-pip.py --user`

`pip3 --version`

#### Instalando AWS CLI
`pip3 install awscli --upgrade --user`

#### Instalando AWS SAM
`sh -c "$(curl -fsSL https://raw.githubusercontent.com/Linuxbrew/install/master/install.sh)"`

`test -d ~/.linuxbrew && eval $(~/.linuxbrew/bin/brew shellenv)`

`test -d /home/linuxbrew/.linuxbrew && eval $(/home/linuxbrew/.linuxbrew/bin/brew shellenv)`

`test -r ~/.bash_profile && echo "eval \$($(brew --prefix)/bin/brew shellenv)" >>~/.bash_profile`

`echo "eval \$($(brew --prefix)/bin/brew shellenv)" >>~/.profile`

`brew tap aws/tap`

`brew install aws-sam-cli`

`sam --version`

#### Instalando o Maven
`sudo apt install maven`

`mvn --version`

### Subindo o banco de dados e a aplicação
1. Iniciar o DynamoDB localmente com Docker. 
`docker run -p 8000:8000 -v $(pwd)/local/dynamodb:/data/ amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath /data`

2. Permitindo o acesso aos diretórios que serão utilizados para subir o dynamodb:
`sudo chmod -R 777 local`

2. Realizar a criação da tabela no DynamoDB. `aws dynamodb create-table --table-name trips --attribute-definitions AttributeName=id,AttributeType=S AttributeName=dateTimeCreation,AttributeType=S --key-schema AttributeName=id,KeyType=HASH AttributeName=dateTimeCreation,KeyType=RANGE --billing-mode PAY_PER_REQUEST --endpoint-url http://localhost:8000`
    
    2.1. Caso a tabela já exista, é possível exclui-la com o seguinte comando: `aws dynamodb delete-table --table-name trips --endpoint-url http://localhost:8000`

3. Reptindo o procedimento de permissões agora para o dynamodb criado:
`sudo chmod -R 777 local`

4. Realize o clone deste repositório:
`git clone https://github.com/bruno-zambotti/trabalho-serverless-architecture-34scj.git`

5. Acesse a raiz do projeto:
`cd cd trabalho-serverless-architecture-34scj`

6. Realizando a compilação do projeto:
`sudo mvn clean install`

7. Para iniciar o AWS SAM API localmente, informe o comando, conforme o seu sistema operacional:
`sam local start-api --env-vars src/test/resources/test_environment_linux.json`

8. Para realizar requisições a função localmente através do API Gateway utilize os exemplos de comandos abaixo:

- Criação de uma viagem:
>`curl --location --request POST 'http://localhost:3000/trips' \
 --header 'Content-Type: application/json' \
 --header 'Content-Type: text/plain' \
 --data-raw '{
 	"id": "1",
 	"dateTimeCreation": "2020-05-01",
 	"country": "BRAZIL",
 	"city": "SAO PAULO"
 }'`

- Consulta de uma viagem por id:
>`curl --location --request GET 'http://localhost:3000/trips/1'`

- Consulta de viagens por período:
>`curl --location --request GET 'http://localhost:3000/trips?start=2020-05-01&end=2020-05-05'`

## Deploy

Será necessária a utilização de um bucket do S3 para armazenamento do código fonte que será entrgeue no serviço AWS Lambda.

Para realizar a criação, informe o seguinte comando no terminal:
- Linux ou MacOs:
```bash
export BUCKET_NAME=trips-bucket-334242
aws s3 mb s3://$BUCKET_NAME
```
- Windows (usando PowerShell):
``` 
$env:BUCKET_NAME="trips-bucket-334242"
aws s3 mb s3://$env:BUCKET_NAME    
```
      
Observação: Caso já exista um bucket com o mesmo nome criado, altere os últimos seis número por um número aleatório de sua preferência.

Em seguida, gere o pacote dos fontes e realize o upload para o S3:
- Linux ou MacOs:
```bash
sam package \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket $BUCKET_NAME
```
- Windows (usando PowerShell):
```
sam package \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket $env:BUCKET_NAME
```

Por fim, o seguinte comando criará a stack do Cloudformation e fará a entrega utilizando o SAM.
```bash
sam deploy \
    --template-file packaged.yaml \
    --stack-name serverless-trip \
    --capabilities CAPABILITY_IAM
```

Adicionalmente após a entrega ter sido concluída com sucesso execute o seguinte comando para obter o endpoint do API Gateway:
```bash
aws cloudformation describe-stacks \
    --stack-name serverless-trip \
    --query 'Stacks[].Outputs'
```

Para remover todos os artefatos criados e limpar a workspace do Cloud9, execute os seguintes comandos:
`aws s3 rb s3://$BUCKET_NAME --force` 

`aws cloudformation delete-stack --stack-name serverless-trip`

`sudo rm -rf trabalho-serverless-architecture-34scj`

`sudo rm -rf local`

## Outras informações

### Testando a aplicação via Postman:
Para testar os métodos da aplicação via Postman siga os passos a seguir:
1. Realize a instalação do [programa](https://www.getpostman.com/downloads/).
2. Após a instalação abra o aplicativo.
3. Após o aplicativo abrir, siga as seguintes instruções:

    3.1. Clique em **File** no menu de opções.
  
    3.2. Em seguida clique em **Import**.
  
    3.3. Tenha certeza de que a aba **Import File** está selecionada na janela que abrir, caso não esteja selecione-a.
  
    3.4. Clique no botão **Choose Files** na janela que será aberta.
  
    3.5. Por fim, escolha o arquivo [***"serverless_trips_service.postman_collection.json"***](../master/src/test/resources/serverless_trips_service.postman_collection.json).

4. Após realizar a configuração descrita é só subir a aplicação e realizar as chamadas desejadas.

## Considerações finais
- Se você realizar a criação de uma nova viagem não informando o id explicitamente, o dynamodb se encarregará de criar um Universally Unique IDentifier (UUID) automaticamente.

- Não foi possível atribuir uma policy com as permissões necessárias para criação de buckets de forma genérica, pois os templates do AWS SAM somente preveem as seguintes [policies](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-policy-template-list.html):
- [S3ReadPolicy](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-policy-template-list.html#s3-read-policy)
- [S3WritePolicy](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-policy-template-list.html#s3-write-policy)
- [S3CrudPolicy](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-policy-template-list.html#s3-crud-policy)
- [S3FullAccessPolicy](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-policy-template-list.html#s3-full-access-policy)

Sendo que as voltadas ao S3 não possuem a action necessária `s3:CreateBucket` para permitir que a lambda crie os buckets da forma que foi solicitada no trabalho. Todas elas já partem da premissa de que o bucket esteja criado.

Algumas tentativas de permitir o acesso ao S3 foram testadas, conforme descrito abaixo:
- Criação de role e policy personalizada com os seguintes comandos e templates:
```bash
aws iam create-role --role-name TripRole --assume-role-policy-document file://trip-role.json
```
```bash
aws iam create-policy --policy-name TripPolicy --policy-document file://trip-policy.json
```
```bash
aws iam attach-role-policy --policy-arn arn:aws:iam::060631834257:policy/TripPolicy --role-name TripRole
```
```bash
aws iam attach-role-policy --role-name TripRole --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
```
Ainda houve uma tentativa de associar a Role `AmazonS3FullAccess` a policy da lambda:
```bash
aws iam attach-role-policy --role-name TripRole --policy-arn arn:aws:iam::aws:policy/AmazonS3FullAccess
```
```bash
aws iam attach-role-policy --role-name TripRole --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
```

Logo para não deixar a aplicação não funcional no ambiente, optei por fazer uma adaptação realizando essa [configuração de policy manualmente](../master/attachments/manual-policy/README.md).


## Links:

- #### [Repositório](https://github.com/bruno-zambotti/trabalho-serverless-architecture-34scj)