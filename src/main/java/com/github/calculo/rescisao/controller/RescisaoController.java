package com.github.calculo.rescisao.controller;


import com.github.calculo.rescisao.dto.RescisaoDTO;
import com.github.calculo.rescisao.model.Funcionario;
import com.github.calculo.rescisao.model.RescisaoFuncionario;
import com.github.calculo.rescisao.repository.FuncionarioRepository;
import com.github.calculo.rescisao.repository.RescisaoFuncionarioRepository;
import com.github.calculo.rescisao.service.RescisaoService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/api/rescisao")
//@CrossOrigin(value = "http://localhost:4200")
public class RescisaoController {

    private final RescisaoService rescisaoService;
    private final RescisaoFuncionarioRepository rescisaoFuncionarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    public RescisaoController(RescisaoService rescisaoService, RescisaoFuncionarioRepository rescisaoFuncionarioRepository, FuncionarioRepository funcionarioRepository) {
        this.rescisaoService = rescisaoService;
        this.rescisaoFuncionarioRepository = rescisaoFuncionarioRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @PostMapping("/calcular")
    public ResponseEntity<RescisaoDTO> calcularRescisao(@RequestBody Funcionario funcionario) {
        RescisaoDTO dto = rescisaoService.calcularRescisao(funcionario);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportarPdf(@PathVariable Long id) throws Exception {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        RescisaoFuncionario dto = rescisaoFuncionarioRepository.findByIdFuncionario(f.getRescisaoFuncionario().getFuncionario().getId());
      //  RescisaoDTO dto = rescisaoService.calcularRescisao(f);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        Font titulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font normal = new Font(Font.FontFamily.HELVETICA, 12);

        document.add(new Paragraph("Recibo de Rescisão", titulo));
        document.add(new Paragraph("Nome: " + f.getNome(), normal));
        document.add(new Paragraph("Admissão: " + f.getDataAdmissao().format(DateTimeFormatter.ISO_DATE), normal));
        document.add(new Paragraph("Demissão: " + f.getDataDemissao().format(DateTimeFormatter.ISO_DATE), normal));
        document.add(new Paragraph("Tipo de Rescisão: " + f.getTipoRescisao(), normal));
        document.add(new Paragraph(" ", normal));

        document.add(new Paragraph("Saldo Salário: R$ " + dto.getSaldoSalario(), normal));
        document.add(new Paragraph("Férias Proporcionais: R$ " + dto.getFeriasProporcionais(), normal));
        document.add(new Paragraph("Férias Vencidas: R$ " + dto.getFeriasVencidas(), normal));
        document.add(new Paragraph("Décimo Terceiro: R$ " + dto.getDecimoTerceiro(), normal));
        document.add(new Paragraph("Aviso Prévio: R$ " + dto.getAvisoPrevio(), normal));
        document.add(new Paragraph("Multa FGTS: R$ " + dto.getMultaFgts(), normal));
        document.add(new Paragraph(" ", normal));
        document.add(new Paragraph("Total: R$ " + dto.getTotal(), titulo));

        document.close();

        byte[] pdfBytes = baos.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rescisao_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
