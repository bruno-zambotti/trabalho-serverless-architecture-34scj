# Trabalho da disciplina de Serverless Architecture 34SCJ.
**Aluno:** Bruno Delphino Zambotti (RM 334242)

**Professor:** Peterson de Oliveira Larentis.

**Descrição:** Projeto do trabalho final da disciplina de Serverless Architecture da turma 34SCJ da FIAP.

## Tecnologias e ferramentas utilizadas
- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Para compilar e executar os códigos descritos nesta aplicação.
- [Maven](https://maven.apache.org/install.html) - Para gerenciamento das dependências da aplicação.
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
> - Retornar no body da resposta o id da Trip e uma url de
um bucket (Amazon S3) que será utilizado para posteriormente
subir as fotos da viagem. O nome do bucket deve ser gerado com a
seguinte sintaxe: ```<trip-country>-<trip-city>-<date>-<6-digit-random-number>```

- RF3 - Permitir a busca de dados das viagens realizadas por período:
> - Na requição o período deverá ser recebido via query parameter: ```/trips?start=YYYY-MM-DD&end=YYYY-MM-DD```
> - Retornar no header da resposta o HTTP Status Code 200 - OK.
> - Retornar no body da resposta uma lista contendo os resultados (todos os campos da entidade).
> - Em caso de não retornar resultados para os parâmetros informados, retornar uma lista vazia, mantendo o HTTP Status Code 200 - OK.

- RF4 - Permitir a busca de dados de uma viagem por id.
> - Na requição o período deverá ser recebido via path parameter: ```/trips/{id}```
> - Retornar somente os dados da viagem referente ao id informado.
> - Caso os registros da viagem sejam encontrados, deverá ser retornado o HTTP Status Code 200 - OK e no body da resposta os dados da viagem.
> - Caso os registros da viagem não sejam encontrados, deverá ser retornado o HTTP Status Code 404 - Not Found, e o body da resposta como vazio.

### Requisitos não funcionais:
- RNF1 - O trabalho deverá conter um arquivo README com as instruções para rodar a aplicação localmente e também na AWS.

- RNF2 - A aplicação deve ser capaz de ser executada com a Stack completa localmente.

- RNF3 - Deverá ser possível implantar a aplicação na AWS através dos comandos do AWS SAM.

## Exemplo de Funcionamento
![](example.gif)

## Funcionalidades:

- Armazenar os dados de uma viagem
```(POST -> /trips)```

- Consultar as informações das viagens por um período
```(GET -> /trips?start=YYYY-MM-DD&end=YYYY-MM-DD)```

- Consultar as informações de uma viagem
```(GET -> /trips/{id})```

## Execução local, empacotamento e entrega

### Execução local

#### Realizando uma requisição na função localmente através do API Gateway
1. Iniciar o DynamoDB localmente com Docker. 
- Linux ou MacOs: `docker run -p 8000:8000 -v $(pwd)/local/dynamodb:/data/ amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath /data`
- Windows (Powershell): `docker run -p 8000:8000 -v $pwd\local\dynamodb:\data\ amazon\dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath \data`

2. Realizar a criação da tabela no DynamoDB. `aws dynamodb create-table --table-name trips --attribute-definitions AttributeName=id,AttributeType=S AttributeName=dateTimeCreation,AttributeType=S --key-schema AttributeName=id,KeyType=HASH AttributeName=dateTimeCreation,KeyType=RANGE --billing-mode PAY_PER_REQUEST --endpoint-url http://localhost:8000`
    
    2.1. Caso a tabela já exista, é possível exclui-la com o seguinte comando: `aws dynamodb delete-table --table-name trips --endpoint-url http://localhost:8000`

3. Para iniciar o AWS SAM API localmente, informe o comando, conforme o seu sistema operacional:
 - MacOS: `sam local start-api --env-vars src/test/resources/test_environment_mac.json`
 - Windows: `sam local start-api --env-vars src\test\resources\test_environment_windows.json`
 - Linux: `sam local start-api --env-vars src/test/resources/test_environment_linux.json`

## Deploy

Será necessária a utilização de um bucket do S3 para armazenamento do código fonte que será entrgeue no serviço AWS Lambda.

Para realizar a criação, informe o seguinte comando no terminal:
- Linux ou MacOs:
```bash
export BUCKET_NAME=trips_bucket_334242
aws s3 mb s3://$BUCKET_NAME
```
- Windows (usando PowerShell):
``` 
$env:BUCKET_NAME="trips_bucket_334242"
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
- Linux ou MacOs:
```bash
sam deploy \
    --template-file packaged.yaml \
    --stack-name serverless-trip \
    --capabilities CAPABILITY_IAM
```
- Windows (usando PowerShell):
```
sam deploy \
    --template-file packaged.yaml \
    --stack-name serverless-trip \
    --capabilities CAPABILITY_IAM
```

Adicionalmente após a entrega ter siso concluída com sucesso execute o seguinte comando para obter o endpoint do API Gateway:
- Linux ou MacOs:
```bash
aws cloudformation describe-stacks \
    --stack-name sam-tripsHandler \
    --query 'Stacks[].Outputs'
```
- Windows (usando PowerShell):
```
aws cloudformation describe-stacks \
    --stack-name sam-tripsHandler \
    --query 'Stacks[].Outputs'
```

## Links:

- #### [Repositório](https://github.com/bruno-zambotti/trabalho-serverless-architecture-34scj)